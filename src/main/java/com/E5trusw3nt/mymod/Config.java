package com.E5trusw3nt.mymod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SoundGearMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_HEADPHONES = BUILDER
            .comment("是否启用耳机物品")
            .define("enableHeadphones", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableHeadphones;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableHeadphones = ENABLE_HEADPHONES.get();
    }
}