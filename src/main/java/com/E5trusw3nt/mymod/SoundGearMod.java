package com.E5trusw3nt.mymod;

import com.E5trusw3nt.mymod.item.ModItems;
import com.E5trusw3nt.mymod.voice.KeyBindings;
import com.E5trusw3nt.mymod.voice.VoiceChatClientHandler;
import com.E5trusw3nt.mymod.voice.VoiceChatNetwork;
import com.E5trusw3nt.mymod.voice.VoiceChatServerHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.slf4j.Logger;

@Mod(SoundGearMod.MODID)
public class SoundGearMod {
    public static final String MODID = "soundgear";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SoundGearMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("SoundGear 模组已加载！");
        // 注册网络包
        event.enqueueWork(VoiceChatNetwork::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.HEADPHONES);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("SoundGear 服务端已启动");
    }

    // 玩家加入服务器时通知同频道玩家
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            VoiceChatServerHandler.onPlayerJoin(serverPlayer);
        }
    }

    // 玩家离开服务器时通知同频道玩家
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            VoiceChatServerHandler.onPlayerLeave(serverPlayer);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("SoundGear 客户端已加载");
            // 注册按键绑定事件监听
            MinecraftForge.EVENT_BUS.register(KeyBindings.class);
            // 注册客户端关闭时的清理
            Runtime.getRuntime().addShutdownHook(new Thread(VoiceChatClientHandler::cleanup));
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            KeyBindings.register(event);
        }
    }
}