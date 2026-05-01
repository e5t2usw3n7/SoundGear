package com.E5trusw3nt.mymod.voice;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 语音状态包
 * 用于同步玩家的语音状态（开始/停止说话、加入/离开频道等）
 * 
 * 这个包和 VoiceDataPacket 的区别是：
 * - VoiceDataPacket 传输实际的音频数据
 * - VoiceStatusPacket 只传输状态信息（没有音频）
 * 
 * 客户端收到这个包后，可以用来显示UI提示
 * 比如"xxx开始说话了"、"xxx离开了频道"
 */
public class VoiceStatusPacket {
    /**
     * 语音状态枚举
     * SPEAKING_START - 开始说话（按下了L键）
     * SPEAKING_STOP - 停止说话（松开了L键）
     * NO_HEADPHONE - 没戴耳机（客户端尝试说话但没戴耳机时的服务端响应）
     * CHANNEL_JOIN - 加入频道（玩家上线或换了耳机颜色）
     * CHANNEL_LEAVE - 离开频道（玩家下线）
     */
    public enum Status {
        SPEAKING_START,
        SPEAKING_STOP,
        NO_HEADPHONE,
        CHANNEL_JOIN,
        CHANNEL_LEAVE
    }

    /** 相关玩家的UUID */
    private final UUID playerId;

    /** 相关的耳机颜色 */
    private final int headphoneColor;

    /** 当前状态 */
    private final Status status;

    /**
     * 构造函数
     * 
     * @param playerId 玩家UUID
     * @param headphoneColor 耳机颜色
     * @param status 当前状态
     */
    public VoiceStatusPacket(UUID playerId, int headphoneColor, Status status) {
        this.playerId = playerId;
        this.headphoneColor = headphoneColor;
        this.status = status;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getHeadphoneColor() {
        return headphoneColor;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * 编码方法
     * 比 VoiceDataPacket 简单多了，因为不需要传输音频数据
     * 
     * @param buf 网络缓冲区
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeInt(headphoneColor);
        buf.writeEnum(status);
    }

    /**
     * 解码方法
     * 注意 readEnum 会自动根据序号还原枚举值
     * 所以枚举的顺序不能随便改，不然会读到错误的状态（坑啊）
     * 
     * @param buf 网络缓冲区
     * @return 解码后的 VoiceStatusPacket 对象
     */
    public static VoiceStatusPacket decode(FriendlyByteBuf buf) {
        UUID playerId = buf.readUUID();
        int color = buf.readInt();
        Status status = buf.readEnum(Status.class);
        return new VoiceStatusPacket(playerId, color, status);
    }

    /**
     * 处理方法
     * 状态包只在客户端处理，用来更新UI显示
     * 
     * @param ctx 网络事件上下文
     */
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 客户端处理：更新语音状态显示
            VoiceChatClientHandler.handleStatusUpdate(this);
        });
        ctx.get().setPacketHandled(true);
    }
}