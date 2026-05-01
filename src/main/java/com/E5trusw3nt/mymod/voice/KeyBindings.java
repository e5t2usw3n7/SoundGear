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
 * 按键绑定注册与处理
 * L 键：按住说话（Push-to-Talk）
 */
@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final String CATEGORY = "key.categories." + SoundGearMod.MODID;

    public static final KeyMapping VOICE_TALK = new KeyMapping(
            "key." + SoundGearMod.MODID + ".voice_talk",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            CATEGORY
    );

    private static boolean wasPressed = false;

    /**
     * 注册按键映射
     */
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(VOICE_TALK);
    }

    /**
     * 每 tick 检测按键状态变化
     * 按下 L 键开始说话，松开停止
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