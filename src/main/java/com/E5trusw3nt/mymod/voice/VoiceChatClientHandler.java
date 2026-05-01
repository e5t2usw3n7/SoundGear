package com.E5trusw3nt.mymod.voice;

import com.E5trusw3nt.mymod.item.HeadphoneItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.sound.sampled.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 客户端语音聊天处理器
 * 负责麦克风录音、音频发送、语音播放
 * 
 * 这个类是整个语音系统最复杂的部分，涉及Java的音频API（javax.sound.sampled）
 * 说实话我一开始完全不知道Java还能录音，查了好多资料才搞明白
 * 
 * 主要功能：
 * 1. 初始化音频设备（麦克风和扬声器）
 * 2. 按下L键时开始录音并发送
 * 3. 接收其他人的语音数据并播放
 * 4. 简单的静音检测（避免发送无声数据浪费带宽）
 * 
 * 注意：这个类只在客户端加载，服务端没有这些功能
 */
public class VoiceChatClientHandler {
    /**
     * 音频格式
     * 16000Hz 采样率，16位深度，单声道，有符号，小端序
     * 
     * 选这些参数的原因：
     * - 16000Hz：语音通话够用了，不需要CD质量（44100Hz）
     * - 16位：比8位清晰很多，但比32位省空间
     * - 单声道：说话只有一个声源，不需要立体声
     * - 有符号小端序：Java标准格式
     */
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(16000.0f, 16, 1, true, false);

    /** 音频缓冲区大小（字节） */
    private static final int BUFFER_SIZE = 4096;

    /** 单个音频包的最大大小 */
    private static final int MAX_AUDIO_LENGTH = BUFFER_SIZE * 4;

    /** 麦克风数据线 */
    private static TargetDataLine microphone;

    /** 扬声器数据线 */
    private static SourceDataLine speaker;

    /** 是否正在传输语音（用AtomicBoolean保证线程安全） */
    private static final AtomicBoolean isTransmitting = new AtomicBoolean(false);

    /** 音频设备是否已初始化 */
    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);

    /** 录音线程 */
    private static Thread captureThread;

    /** 播放线程 */
    private static Thread playbackThread;

    /**
     * 初始化音频设备
     * 尝试获取麦克风和扬声器的访问权限
     * 
     * @return true 如果初始化成功，false 如果设备不可用
     */
    public static boolean init() {
        if (isInitialized.get()) return true;
        try {
            // 初始化麦克风
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
            if (!AudioSystem.isLineSupported(micInfo)) {
                return false;
            }
            microphone = (TargetDataLine) AudioSystem.getLine(micInfo);

            // 初始化扬声器
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, AUDIO_FORMAT);
            if (!AudioSystem.isLineSupported(speakerInfo)) {
                return false;
            }
            speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);

            isInitialized.set(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 开始语音传输（按下L键时调用）
     * 
     * 流程：
     * 1. 检查音频设备是否初始化
     * 2. 检查玩家是否佩戴耳机
     * 3. 更新状态为"正在传输"
     * 4. 通知服务端开始说话
     * 5. 启动录音线程
     */
    public static void startTransmitting() {
        if (!isInitialized.get()) {
            if (!init()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    mc.player.displayClientMessage(
                            Component.literal("§c[SoundGear] §7无法初始化麦克风，请检查音频设备"), true);
                }
                return;
            }
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // 检查是否佩戴耳机
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) {
            player.displayClientMessage(
                    Component.literal("§c[SoundGear] §7你需要佩戴耳机才能使用语音"), true);
            return;
        }

        if (isTransmitting.compareAndSet(false, true)) {
            player.displayClientMessage(
                    Component.literal("§a[SoundGear] §7语音已开启 - 正在说话..."), true);

            // 通知服务端开始说话
            int color = HeadphoneItem.getHeadphoneColor(helmet);
            VoiceChatNetwork.sendVoiceToServer(
                    new VoiceDataPacket(player.getUUID(), color, new byte[0]));

            // 启动录音线程
            captureThread = new Thread(() -> captureAndSendAudio(), "SoundGear-VoiceCapture");
            captureThread.setDaemon(true);
            captureThread.start();
        }
    }

    /**
     * 停止语音传输（松开L键时调用）
     * 
     * 流程：
     * 1. 更新状态为"停止传输"
     * 2. 通知服务端停止说话
     * 3. 关闭麦克风
     */
    public static void stopTransmitting() {
        if (isTransmitting.compareAndSet(true, false)) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(
                        Component.literal("§7[SoundGear] §7语音已关闭"), true);

                ItemStack helmet = player.getInventory().getArmor(3);
                if (helmet.getItem() instanceof HeadphoneItem) {
                    int color = HeadphoneItem.getHeadphoneColor(helmet);
                    // 通知服务端停止说话
                    VoiceChatNetwork.CHANNEL.sendToServer(
                            new VoiceDataPacket(player.getUUID(), color, new byte[0]));
                }
            }

            // 关闭麦克风
            if (microphone != null && microphone.isOpen()) {
                microphone.stop();
                microphone.close();
            }
        }
    }

    /**
     * 捕获麦克风音频并发送到服务端
     * 这个方法在独立的线程里运行，循环录音直到 isTransmitting 变为 false
     * 
     * 每次循环：
     * 1. 从麦克风读取音频数据
     * 2. 检测是否有声音（静音检测）
     * 3. 如果有声音，打包发送到服务端
     */
    private static void captureAndSendAudio() {
        try {
            microphone.open(AUDIO_FORMAT);
            microphone.start();

            byte[] buffer = new byte[BUFFER_SIZE];
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            ItemStack helmet = player.getInventory().getArmor(3);
            int color = HeadphoneItem.getHeadphoneColor(helmet);

            while (isTransmitting.get()) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    // 检查是否有声音（简单的静音检测）
                    if (hasSound(buffer, bytesRead)) {
                        byte[] audioData = new byte[bytesRead];
                        System.arraycopy(buffer, 0, audioData, 0, bytesRead);
                        VoiceChatNetwork.sendVoiceToServer(
                                new VoiceDataPacket(player.getUUID(), color, audioData));
                    }
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (microphone != null && microphone.isOpen()) {
                microphone.stop();
                microphone.close();
            }
        }
    }

    /**
     * 简单的声音检测
     * 通过计算音量平均值来判断是否有人说话
     * 如果平均音量低于阈值，就认为是静音，不发送数据
     * 
     * 这个算法虽然简单，但效果还行，能过滤掉大部分背景噪音
     * 当然如果环境很吵的话还是会发送噪音的，以后可以考虑加个降噪
     * 
     * @param buffer 音频缓冲区
     * @param length 实际读取的字节数
     * @return true 如果检测到声音，false 如果是静音
     */
    private static boolean hasSound(byte[] buffer, int length) {
        long sum = 0;
        for (int i = 0; i < length; i += 2) {
            if (i + 1 < length) {
                short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
                sum += Math.abs(sample);
            }
        }
        double average = (double) sum / (length / 2);
        return average > 500; // 音量阈值
    }

    /**
     * 处理来自服务端的语音数据
     * 这个方法在 VoiceDataPacket 的 handle 方法里被调用
     * 
     * 流程：
     * 1. 检查音频数据是否有效
     * 2. 验证接收者是否佩戴耳机
     * 3. 验证颜色是否匹配（同频道）
     * 4. 播放音频
     * 
     * @param packet 语音数据包
     */
    public static void handleVoiceData(VoiceDataPacket packet) {
        if (packet.getAudioData() == null || packet.getAudioData().length == 0) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // 验证接收者也佩戴了同颜色耳机
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) return;

        int myColor = HeadphoneItem.getHeadphoneColor(helmet);
        if (myColor != packet.getHeadphoneColor()) return;

        // 播放接收到的音频
        playAudio(packet.getAudioData());
    }

    /**
     * 播放音频数据
     * 把接收到的音频字节写入扬声器数据线
     * 
     * @param audioData PCM音频字节数组
     */
    private static void playAudio(byte[] audioData) {
        try {
            if (!speaker.isOpen()) {
                speaker.open(AUDIO_FORMAT);
                speaker.start();
            }
            speaker.write(audioData, 0, audioData.length);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理语音状态更新
     * 这个方法在 VoiceStatusPacket 的 handle 方法里被调用
     * 用来在客户端显示状态消息（聊天栏或action bar）
     * 
     * @param packet 语音状态包
     */
    public static void handleStatusUpdate(VoiceStatusPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // 通过 UUID 获取玩家名称
        // mc.level.getPlayerByUUID 只能获取同一维度的玩家
        // mc.getConnection().getPlayerInfo 是备用方案，从玩家列表查找
        String playerName = "未知玩家";
        if (mc.level != null) {
            net.minecraft.world.entity.player.Player otherPlayer = mc.level.getPlayerByUUID(packet.getPlayerId());
            if (otherPlayer != null) {
                playerName = otherPlayer.getDisplayName().getString();
            } else {
                // 如果不在同一维度，尝试从连接信息获取
                net.minecraft.client.multiplayer.PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(packet.getPlayerId());
                if (playerInfo != null) {
                    playerName = playerInfo.getProfile().getName();
                }
            }
        }

        switch (packet.getStatus()) {
            case SPEAKING_START:
                // 显示谁开始说话了（action bar，不会刷屏）
                mc.player.displayClientMessage(
                        Component.literal("§e[SoundGear] §7" + playerName + " 正在说话..."), true);
                break;
            case SPEAKING_STOP:
                // 说话停止后清空 action bar（显示空字符串来清除）
                mc.player.displayClientMessage(Component.literal(""), true);
                break;
            case NO_HEADPHONE:
                mc.player.displayClientMessage(
                        Component.literal("§c[SoundGear] §7你需要佩戴耳机才能使用语音"), true);
                break;
            case CHANNEL_JOIN:
                // 在聊天栏显示加入频道通知
                mc.player.displayClientMessage(
                        Component.literal("§a[SoundGear] §7" + playerName + " 加入了你的语音频道"), false);
                break;
            case CHANNEL_LEAVE:
                // 在聊天栏显示离开频道通知
                mc.player.displayClientMessage(
                        Component.literal("§c[SoundGear] §7" + playerName + " 离开了你的语音频道"), false);
                break;
        }
    }

    /**
     * 清理资源
     * 在游戏关闭时调用，确保麦克风和扬声器被正确释放
     * 不然可能会出现麦克风被占用的问题（别问我怎么知道的）
     */
    public static void cleanup() {
        stopTransmitting();
        if (speaker != null && speaker.isOpen()) {
            speaker.drain();
            speaker.close();
        }
        isInitialized.set(false);
    }
}