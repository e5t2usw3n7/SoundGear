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
 * 语音聊天网络通道
 * 处理客户端和服务端之间的语音数据传输
 */
public class VoiceChatNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SoundGearMod.MODID, "voice_chat"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        // 注册语音数据包（客户端 -> 服务端）
        CHANNEL.registerMessage(packetId++, VoiceDataPacket.class,
                VoiceDataPacket::encode, VoiceDataPacket::decode,
                VoiceDataPacket::handle);

        // 注册语音状态包（服务端 -> 客户端）
        CHANNEL.registerMessage(packetId++, VoiceStatusPacket.class,
                VoiceStatusPacket::encode, VoiceStatusPacket::decode,
                VoiceStatusPacket::handle);
    }

    /**
     * 发送语音数据到服务端
     */
    public static void sendVoiceToServer(VoiceDataPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    /**
     * 服务端发送语音数据到指定玩家
     */
    public static void sendVoiceToPlayer(ServerPlayer player, VoiceDataPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * 发送语音状态通知到客户端
     */
    public static void sendVoiceStatusToPlayer(ServerPlayer player, VoiceStatusPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}