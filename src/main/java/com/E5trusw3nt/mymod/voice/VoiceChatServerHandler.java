package com.E5trusw3nt.mymod.voice;

import com.E5trusw3nt.mymod.item.HeadphoneItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * 服务端语音聊天处理
 * 负责接收客户端的语音数据，并转发给相同颜色耳机的玩家
 */
public class VoiceChatServerHandler {

    /**
     * 处理来自客户端的语音数据
     * 只转发给佩戴相同颜色耳机的玩家
     */
    public static void handleVoiceData(ServerPlayer sender, VoiceDataPacket packet) {
        if (sender == null) return;

        // 检查发送者是否佩戴耳机
        ItemStack senderHelmet = sender.getInventory().getArmor(3); // 头盔槽位（索引3）
        if (!(senderHelmet.getItem() instanceof HeadphoneItem)) {
            VoiceChatNetwork.sendVoiceStatusToPlayer(sender,
                    new VoiceStatusPacket(sender.getUUID(), 0, VoiceStatusPacket.Status.NO_HEADPHONE));
            return;
        }

        int senderColor = HeadphoneItem.getHeadphoneColor(senderHelmet);
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        // 遍历所有在线玩家，转发给佩戴相同颜色耳机的玩家
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // 不转发给自己
            if (player.getUUID().equals(sender.getUUID())) continue;

            ItemStack playerHelmet = player.getInventory().getArmor(3);
            if (!(playerHelmet.getItem() instanceof HeadphoneItem)) continue;

            int playerColor = HeadphoneItem.getHeadphoneColor(playerHelmet);
            if (senderColor == playerColor) {
                // 同颜色频道，转发语音数据
                VoiceChatNetwork.sendVoiceToPlayer(player,
                        new VoiceDataPacket(sender.getUUID(), senderColor, packet.getAudioData()));
            }
        }
    }

    /**
     * 玩家上线时通知同频道玩家
     */
    public static void onPlayerJoin(ServerPlayer player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) return;

        int color = HeadphoneItem.getHeadphoneColor(helmet);
        broadcastStatus(player, color, VoiceStatusPacket.Status.CHANNEL_JOIN);
    }

    /**
     * 玩家下线时通知同频道玩家
     */
    public static void onPlayerLeave(ServerPlayer player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) return;

        int color = HeadphoneItem.getHeadphoneColor(helmet);
        broadcastStatus(player, color, VoiceStatusPacket.Status.CHANNEL_LEAVE);
    }

    /**
     * 广播语音状态给同颜色耳机的玩家
     */
    private static void broadcastStatus(ServerPlayer target, int color, VoiceStatusPacket.Status status) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.getUUID().equals(target.getUUID())) continue;

            ItemStack helmet = player.getInventory().getArmor(3);
            if (!(helmet.getItem() instanceof HeadphoneItem)) continue;

            int playerColor = HeadphoneItem.getHeadphoneColor(helmet);
            if (color == playerColor) {
                VoiceChatNetwork.sendVoiceStatusToPlayer(player,
                        new VoiceStatusPacket(target.getUUID(), color, status));
            }
        }
    }
}