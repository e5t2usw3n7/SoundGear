package com.E5trusw3nt.mymod.voice;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 语音数据包
 * 包含说话者UUID、耳机颜色和音频数据
 */
public class VoiceDataPacket {
    private final UUID senderId;
    private final int headphoneColor;
    private final byte[] audioData;

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

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(senderId);
        buf.writeInt(headphoneColor);
        buf.writeInt(audioData.length);
        buf.writeBytes(audioData);
    }

    public static VoiceDataPacket decode(FriendlyByteBuf buf) {
        UUID senderId = buf.readUUID();
        int color = buf.readInt();
        int length = buf.readInt();
        byte[] data = new byte[length];
        buf.readBytes(data);
        return new VoiceDataPacket(senderId, color, data);
    }

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