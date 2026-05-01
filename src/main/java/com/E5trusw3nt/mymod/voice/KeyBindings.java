package com.E5trusw3nt.mymod.voice;

import com.E5trusw3nt.mymod.SoundGearMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * 按键绑定类
 * 负责注册和检测 L 键的按下/松开
 * 
 * L 键的功能是"按住说话"（Push-to-Talk, PTT）
 * 按下时开始录音并发送，松开时停止
 * 这个设计和很多语音软件（比如Discord、TeamSpeak）的PTT模式一样
 * 
 * 注意：这个类只在客户端加载（@OnlyIn(Dist.CLIENT)）
 * 因为按键绑定只有客户端才有意义
 */
@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    /**
     * 按键分类名称
     * 会在游戏的按键设置界面显示为一个独立的分类
     * 这样玩家能很容易找到我们模组的按键设置
     */
    public static final String CATEGORY = "key.categories." + SoundGearMod.MODID;

    /**
     * 语音通话按键映射
     * 默认绑定到 L 键（GLFW.GLFW_KEY_L）
     * 
     * 参数说明：
     * - KeyConflictContext.IN_GAME：只在游戏内生效（不在聊天框、GUI等界面）
     * - KeyModifier.NONE：没有组合键（不需要Shift、Ctrl等）
     * - InputConstants.Type.KEYSYM：使用键盘按键（而不是鼠标按钮等）
     * 
     * 玩家可以在游戏的按键设置里修改这个绑定
     */
    public static final KeyMapping VOICE_TALK = new KeyMapping(
            "key." + SoundGearMod.MODID + ".voice_talk",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            CATEGORY
    );

    /**
     * 上一帧的按键状态
     * 用来检测按键状态的变化（按下/松开）
     * 如果只检查当前是否按下，会一直触发；我们需要的是"刚按下"和"刚松开"的瞬间
     */
    private static boolean wasPressed = false;

    /**
     * 注册按键映射到 Minecraft
     * 在 RegisterKeyMappingsEvent 事件里调用
     * 这样按键才会出现在设置 -> 按键绑定 里面
     * 
     * @param event 按键映射注册事件
     */
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(VOICE_TALK);
    }

    /**
     * 每 tick 检测按键状态变化
     * 这个方法会被 Forge 的事件系统每帧调用
     * 
     * 逻辑很简单：
     * - 如果这一帧按下了，上一帧没按下 -> 触发"按下"事件（开始说话）
     * - 如果这一帧没按下，上一帧按下了 -> 触发"松开"事件（停止说话）
     * 
     * 只检查 Phase.END 是因为一帧可能会触发两次事件（开始和结束）
     * 我们只需要在帧结束时检查一次就够了
     * 
     * @param event 客户端 tick 事件
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        boolean isPressed = VOICE_TALK.isDown();
        if (isPressed && !wasPressed) {
            // 按下：开始语音传输
            VoiceChatClientHandler.startTransmitting();
        } else if (!isPressed && wasPressed) {
            // 松开：停止语音传输
            VoiceChatClientHandler.stopTransmitting();
        }
        wasPressed = isPressed;
    }
}