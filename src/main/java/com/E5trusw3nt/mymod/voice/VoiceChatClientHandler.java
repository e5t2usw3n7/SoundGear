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
 * 客户端语音聊天处理
 * 负责麦克风录音、语音发送、音频播放
 */
public class VoiceChatClientHandler {
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(16000.0f, 16, 1, true, false);
    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_AUDIO_LENGTH = BUFFER_SIZE * 4; // 最大音频包大小

    private static TargetDataLine microphone;
    private static SourceDataLine speaker;
    private static final AtomicBoolean isTransmitting = new AtomicBoolean(false);
    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static Thread captureThread;
    private static Thread playbackThread;

    /**
     * 初始化音频设备
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
     * 开始语音传输（按下 L 键时调用）
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
     * 停止语音传输（松开 L 键时调用）
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
     * 简单的声音检测（通过音量阈值判断是否有人说话）
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
     * 处理来自服务端的语音数据（播放音频）
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
     */
    public static void handleStatusUpdate(VoiceStatusPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        String colorHex = String.format("#%06X", packet.getHeadphoneColor() & 0xFFFFFF);
        switch (packet.getStatus()) {
            case SPEAKING_START:
                // 可以在这里显示说话者头顶的语音图标
                break;
            case SPEAKING_STOP:
                break;
            case NO_HEADPHONE:
                break;
            case CHANNEL_JOIN:
                mc.player.displayClientMessage(
                        Component.literal("§a[SoundGear] §7一位玩家加入了你的语音频道 " + colorHex), false);
                break;
            case CHANNEL_LEAVE:
                mc.player.displayClientMessage(
                        Component.literal("§c[SoundGear] §7一位玩家离开了你的语音频道 " + colorHex), false);
                break;
        }
    }

    /**
     * 清理资源
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