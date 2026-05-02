package com.E5trusw3nt.mymod.voice;

import com.E5trusw3nt.mymod.SoundGearMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 语音聊天网络通道类
 * 负责管理客户端和服务端之间的网络通信
 * 
 * Forge 的网络系统有点绕，简单来说就是：
 * 1. 你需要一个"通道"（SimpleChannel）来发送和接收数据包
 * 2. 每种数据包都要注册到这个通道上，并提供编解码方法
 * 3. 客户端和服务端通过这个通道来交换数据
 * 
 * 目前注册了两种数据包：
 * - VoiceDataPacket：传输实际的音频数据
 * - VoiceStatusPacket：同步语音状态（开始说话、停止说话等）
 */
public class VoiceChatNetwork {
    /**
     * 协议版本号
     * 客户端和服务端的版本号必须匹配才能通信
     * 我们这里是单人游戏和局域网，所以简单设成 "1" 就行
     * 如果以后改了数据包格式，记得更新这个版本号
     */
    private static final String PROTOCOL_VERSION = "1";

    /**
     * 网络通道对象
     * 所有语音相关的数据包都通过这个通道发送
     * 
     * 构造参数：
     * 1. 通道的唯一ID（ResourceLocation 格式）
     * 2. 版本号获取方法
     * 3. 版本号比较方法（客户端用）
     * 4. 版本号比较方法（服务端用）
     * 
     * ResourceLocation(String, String) 构造函数在 1.20.1 中被标记为 deprecated(forRemoval=true)
     * 这个地方报了个warning，AI 说这个是在之后的版本被弃用了，但目前还没有替代方案，所以暂时先 suppress 了这个 warning
     */
    @SuppressWarnings("removal")
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SoundGearMod.MODID, "voice_chat"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    /**
     * 数据包ID计数器
     * 每注册一个数据包，ID就会自增
     * 这个ID是用来区分不同类型的包的，接收端根据ID来选择对应的处理方法
     */
    private static int packetId = 0;

    /**
     * 注册所有数据包到通道
     * 必须在 commonSetup 事件里调用（通过 enqueueWork 包裹）
     * 
     * 注册顺序很重要！客户端和服务端的注册顺序必须一致
     * 不然会出现 "收到数据包但不知道怎么解码" 的错误
     * （调试了好久才发现是顺序问题……）
     */
    public static void register() {
        // 注册语音数据包（客户端 -> 服务端）
        // registerMessage 的参数：ID, 包类, 编码方法, 解码方法, 处理方法
        CHANNEL.registerMessage(packetId++, VoiceDataPacket.class,
                VoiceDataPacket::encode, VoiceDataPacket::decode,
                VoiceDataPacket::handle);

        // 注册语音状态包（服务端 -> 客户端）
        CHANNEL.registerMessage(packetId++, VoiceStatusPacket.class,
                VoiceStatusPacket::encode, VoiceStatusPacket::decode,
                VoiceStatusPacket::handle);
    }

    /**
     * 客户端发送语音数据到服务端
     * 这个方法会在按下 L 键录音时被调用
     * 
     * @param packet 语音数据包，里面包含音频数据和耳机颜色
     */
    public static void sendVoiceToServer(VoiceDataPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    /**
     * 服务端发送语音数据到指定玩家
     * 这个在 VoiceChatServerHandler 里调用，用来转发语音给同频道的玩家
     * 
     * @param player 目标玩家
     * @param packet 语音数据包
     */
    public static void sendVoiceToPlayer(ServerPlayer player, VoiceDataPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * 服务端发送语音状态通知到客户端
     * 比如"某人开始说话了"、"某人加入频道了"这类通知
     * 
     * @param player 目标玩家
     * @param packet 语音状态包
     */
    public static void sendVoiceStatusToPlayer(ServerPlayer player, VoiceStatusPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}