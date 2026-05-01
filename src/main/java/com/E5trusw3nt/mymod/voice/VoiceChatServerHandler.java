package com.E5trusw3nt.mymod.voice;

import com.E5trusw3nt.mymod.item.HeadphoneItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端语音聊天处理器
 * 负责接收客户端发来的语音数据，然后转发给同颜色耳机的其他玩家
 * 
 * 这个类是语音系统在服务端的核心，逻辑其实不复杂：
 * 1. 收到客户端的语音包
 * 2. 检查发送者是否戴着耳机
 * 3. 遍历所有在线玩家，找到戴同颜色耳机的
 * 4. 把语音数据转发给他们
 * 
 * 就像一个对讲机基站，只负责转发，不负责处理音频内容
 */
public class VoiceChatServerHandler {
    /**
     * 正在说话的玩家UUID集合
     * 用 ConcurrentHashMap.newKeySet() 创建线程安全的 Set
     * 因为网络包可能在不同线程处理，普通的 HashSet 会出问题
     */
    private static final Set<UUID> speakingPlayers = ConcurrentHashMap.newKeySet();

    /**
     * 处理来自客户端的语音数据
     * 这个方法在 VoiceDataPacket 的 handle 方法里被调用
     * 
     * 流程：
     * 1. 检查发送者是否佩戴耳机
     * 2. 获取发送者的耳机颜色
     * 3. 遍历所有在线玩家
     * 4. 跳过发送者自己（不然会听到自己的回声，很诡异的）
     * 5. 检查其他玩家是否佩戴耳机
     * 6. 比较颜色，如果一样就转发
     * 
     * @param sender 发送语音的玩家
     * @param packet 语音数据包
     */
    public static void handleVoiceData(ServerPlayer sender, VoiceDataPacket packet) {
        if (sender == null) return;

        // 检查发送者是否佩戴耳机
        // getArmor(3) 是头盔槽位，索引0-3分别对应头盔、胸甲、护腿、靴子
        ItemStack senderHelmet = sender.getInventory().getArmor(3);
        if (!(senderHelmet.getItem() instanceof HeadphoneItem)) {
            // 如果没戴耳机，给客户端发个通知
            VoiceChatNetwork.sendVoiceStatusToPlayer(sender,
                    new VoiceStatusPacket(sender.getUUID(), 0, VoiceStatusPacket.Status.NO_HEADPHONE));
            return;
        }

        // 获取发送者的耳机颜色
        int senderColor = HeadphoneItem.getHeadphoneColor(senderHelmet);
        
        // 获取服务器实例
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        // 判断是状态变更还是实际音频数据
        // 空音频包 = 开始/停止说话的状态通知
        // 非空音频包 = 实际的语音数据，需要转发
        if (packet.getAudioData() == null || packet.getAudioData().length == 0) {
            // 空音频包：判断是开始还是停止说话
            // 逻辑：如果该玩家已经在 speakingPlayers 集合中，说明之前在说话，现在要停止
            // 如果不在集合中，说明刚开始说话
            UUID senderId = sender.getUUID();
            boolean wasSpeaking = !speakingPlayers.add(senderId);
            if (wasSpeaking) {
                // 之前在说话 -> 现在停止
                speakingPlayers.remove(senderId);
                broadcastStatus(sender, senderColor, VoiceStatusPacket.Status.SPEAKING_STOP);
            } else {
                // 刚开始说话
                broadcastStatus(sender, senderColor, VoiceStatusPacket.Status.SPEAKING_START);
            }
            return;
        }

        // 实际音频数据：标记为正在说话并转发
        // 如果还没标记过，先标记并广播开始说话
        UUID senderId = sender.getUUID();
        if (speakingPlayers.add(senderId)) {
            broadcastStatus(sender, senderColor, VoiceStatusPacket.Status.SPEAKING_START);
        }

        // 遍历所有在线玩家，转发给佩戴相同颜色耳机的玩家
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // 不转发给自己
            if (player.getUUID().equals(sender.getUUID())) continue;

            // 检查其他玩家是否佩戴耳机
            ItemStack playerHelmet = player.getInventory().getArmor(3);
            if (!(playerHelmet.getItem() instanceof HeadphoneItem)) continue;

            // 获取其他玩家的耳机颜色
            int playerColor = HeadphoneItem.getHeadphoneColor(playerHelmet);
            
            // 如果颜色相同，转发语音数据
            if (senderColor == playerColor) {
                VoiceChatNetwork.sendVoiceToPlayer(player,
                        new VoiceDataPacket(sender.getUUID(), senderColor, packet.getAudioData()));
            }
        }
    }

    /**
     * 玩家加入服务器时的通知
     * 在 SoundGearMod 的 onPlayerLoggedIn 事件里调用
     * 会通知同频道的其他玩家"有人来了"
     * 
     * @param player 刚加入的玩家
     */
    public static void onPlayerJoin(ServerPlayer player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) return;

        int color = HeadphoneItem.getHeadphoneColor(helmet);
        broadcastStatus(player, color, VoiceStatusPacket.Status.CHANNEL_JOIN);
    }

    /**
     * 玩家离开服务器时的通知
     * 在 SoundGearMod 的 onPlayerLoggedOut 事件里调用
     * 会通知同频道的其他玩家"有人走了"
     * 
     * @param player 离开的玩家
     */
    public static void onPlayerLeave(ServerPlayer player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        if (!(helmet.getItem() instanceof HeadphoneItem)) return;

        int color = HeadphoneItem.getHeadphoneColor(helmet);
        broadcastStatus(player, color, VoiceStatusPacket.Status.CHANNEL_LEAVE);
    }

    /**
     * 广播语音状态给同颜色耳机的玩家
     * 这是一个内部工具方法，用来发送频道加入/离开通知的
     * 
     * 逻辑和 handleVoiceData 差不多：
     * 1. 遍历所有在线玩家
     * 2. 跳过目标玩家自己
     * 3. 检查是否佩戴耳机
     * 4. 比较颜色，如果一样就发通知
     * 
     * @param target 相关的玩家（加入/离开的那个）
     * @param color 耳机颜色
     * @param status 状态类型
     */
    private static void broadcastStatus(ServerPlayer target, int color, VoiceStatusPacket.Status status) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // 不给自己发
            if (player.getUUID().equals(target.getUUID())) continue;

            // 检查是否佩戴耳机
            ItemStack helmet = player.getInventory().getArmor(3);
            if (!(helmet.getItem() instanceof HeadphoneItem)) continue;

            // 比较颜色
            int playerColor = HeadphoneItem.getHeadphoneColor(helmet);
            if (color == playerColor) {
                VoiceChatNetwork.sendVoiceStatusToPlayer(player,
                        new VoiceStatusPacket(target.getUUID(), color, status));
            }
        }
    }
}