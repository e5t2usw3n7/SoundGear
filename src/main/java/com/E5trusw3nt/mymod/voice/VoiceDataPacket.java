package com.E5trusw3nt.mymod.voice;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 语音数据包
 * 用于传输实际的音频数据
 * 
 * 这个包包含三个信息：
 * 1. senderId - 谁在说话（UUID）
 * 2. headphoneColor - 说话者的耳机颜色（RGB值）
 * 3. audioData - 实际的音频字节数组
 * 
 * 注意：当 audioData 为空时，这个包代表"状态变更"而不是实际音频
 * （比如开始/停止说话的时候会发一个空包通知服务端）
 */
public class VoiceDataPacket {
    /** 发送者的UUID，用来标识是谁在说话 */
    private final UUID senderId;

    /** 发送者的耳机颜色，用来判断属于哪个频道 */
    private final int headphoneColor;

    /** 音频数据，PCM格式的字节数组 */
    private final byte[] audioData;

    /**
     * 构造函数
     * 
     * @param senderId 发送者UUID
     * @param headphoneColor 耳机颜色RGB值
     * @param audioData 音频字节数组（可以为空数组，表示状态变更）
     */
    public VoiceDataPacket(UUID senderId, int headphoneColor, byte[] audioData) {
        this.senderId = senderId;
        this.headphoneColor = headphoneColor;
        this.audioData = audioData;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public int getHeadphoneColor() {
        return headphoneColor;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    /**
     * 编码方法，把数据包写入网络缓冲区
     * 顺序必须和 decode 一致，不然会乱套
     * 
     * 写入顺序：UUID -> 颜色(int) -> 数据长度(int) -> 音频数据(字节数组)
     * 这里先写长度再写数据，是因为接收端需要知道要读多少字节
     * 
     * @param buf 网络缓冲区
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(senderId);
        buf.writeInt(headphoneColor);
        buf.writeInt(audioData.length);
        buf.writeBytes(audioData);
    }

    /**
     * 解码方法，从网络缓冲区读取数据并构造数据包对象
     * 必须和 encode 的顺序完全对应
     * 
     * @param buf 网络缓冲区
     * @return 解码后的 VoiceDataPacket 对象
     */
    public static VoiceDataPacket decode(FriendlyByteBuf buf) {
        UUID senderId = buf.readUUID();
        int color = buf.readInt();
        int length = buf.readInt();
        byte[] data = new byte[length];
        buf.readBytes(data);
        return new VoiceDataPacket(senderId, color, data);
    }

    /**
     * 处理方法，接收端收到包后会调用这个
     * 
     * 这里有个判断：
     * - 如果是服务端收到（客户端发来的）：交给 VoiceChatServerHandler 处理
     * - 如果是客户端收到（服务端转发的）：交给 VoiceChatClientHandler 播放音频
     * 
     * enqueueWork 是为了让处理逻辑在主线程执行
     * 因为 Minecraft 的很多操作不是线程安全的，直接在网络线程操作可能会出问题
     * 
     * @param ctx 网络事件上下文
     */
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                // 服务端处理：转发给同颜色耳机的玩家
                VoiceChatServerHandler.handleVoiceData(ctx.get().getSender(), this);
            } else {
                // 客户端处理：播放接收到的语音数据
                VoiceChatClientHandler.handleVoiceData(this);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}