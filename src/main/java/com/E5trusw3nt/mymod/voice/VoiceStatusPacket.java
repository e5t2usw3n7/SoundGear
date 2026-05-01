package com.E5trusw3nt.mymod.voice;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 语音状态包
 * 用于通知客户端其他玩家的语音状态（开始/停止说话等）
 */
public class VoiceStatusPacket {
    public enum Status {
        SPEAKING_START,
        SPEAKING_STOP,
        NO_HEADPHONE,
        CHANNEL_JOIN,
        CHANNEL_LEAVE
    }

    private final UUID playerId;
    private final int headphoneColor;
    private final Status status;

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

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeInt(headphoneColor);
        buf.writeEnum(status);
    }

    public static VoiceStatusPacket decode(FriendlyByteBuf buf) {
        UUID playerId = buf.readUUID();
        int color = buf.readInt();
        Status status = buf.readEnum(Status.class);
        return new VoiceStatusPacket(playerId, color, status);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 客户端处理：更新语音状态显示
            VoiceChatClientHandler.handleStatusUpdate(this);
        });
        ctx.get().setPacketHandled(true);
    }
}