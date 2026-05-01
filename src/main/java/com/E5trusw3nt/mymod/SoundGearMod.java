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

/**
 * SoundGear 模组的主入口类
 * 这个类就是整个模组的"老大"，所有东西都从这里开始初始化的
 * Forge 加载模组的时候会先找到这个 @Mod 注解，然后调构造函数
 * 说实话我一开始都不知道构造函数里要写啥，看了好久才搞明白……
 */
@Mod(SoundGearMod.MODID)
public class SoundGearMod {
    /*模组ID，全局唯一标识符，注册啥东西都得带上它 */
    public static final String MODID = "soundgear";

    /*日志工具，用来在控制台输出信息的*/
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 构造函数——Forge 启动时会自动调用这个
     * @param context FMLModLoadingContext，用来获取事件总线和注册配置的
     * 
     * 这里做了几件事：
     * 1. 注册物品（耳机）
     * 2. 把自己挂到 Forge 的事件总线上（这样 @SubscribeEvent 才能生效）
     * 3. 监听 commonSetup 事件来做初始化
     * 4. 把耳机加到创造模式标签页
     * 5. 注册配置文件
     */
    public SoundGearMod(FMLJavaModLoadingContext context) {
        // 拿到模组事件总线，注册各种东西都靠它
        IEventBus modEventBus = context.getModEventBus();

        // commonSetup 会在模组初始化阶段触发，我们在里面注册网络包
        modEventBus.addListener(this::commonSetup);

        // 注册所有自定义物品（目前就一个耳机）
        ModItems.register(modEventBus);

        // 挂到 Forge 的事件总线上，这样 onServerStarting、onPlayerJoin 这些事件才能被触发
        MinecraftForge.EVENT_BUS.register(this);

        // 监听创造模式标签页的事件，用来把耳机塞进去
        modEventBus.addListener(this::addCreative);

        // 注册配置文件（common 类型的），配置项在 Config 类里定义
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /**
     * 模组公共初始化（服务端和客户端都会执行）
     * 主要在这里注册网络包，因为网络包必须在 enqueueWork 里面注册才安全
     * 这个是AI推荐的做法，不然可能会有线程安全问题（虽然这一块代码我也不太懂就是了）
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("SoundGear mod is loaded");
        // 用 enqueueWork 包一下确保在网络线程安全的地方注册
        event.enqueueWork(VoiceChatNetwork::register);
    }

    /**
     * 把物品加到创造模式标签页里
     * 这样玩家在创造模式就能在「战斗」分类里找到耳机了
     * @param event 标签页内容事件
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 只在战斗标签页里添加
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            for (var headphones : ModItems.ALL_HEADPHONES) {
                event.accept(headphones);
            }
        }
    }

    /**
     * 服务端启动事件，目前只是打个日志
     * 以后如果要加什么服务端初始化逻辑可以放这里
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("SoundGear 服务端已启动");
    }

    /**
     * 玩家加入服务器时触发
     * 这里会通知同颜色耳机频道里的其他玩家"有人来了"
     * 这样大家就知道频道里多了一个人
     */
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            VoiceChatServerHandler.onPlayerJoin(serverPlayer);
        }
    }

    /**
     * 玩家离开服务器时触发
     * 通知同频道的玩家"有人走了"
     */
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            VoiceChatServerHandler.onPlayerLeave(serverPlayer);
        }
    }

    /**
     * 客户端专属的事件处理内部类
     * 只在客户端加载（Dist.CLIENT），服务端不会加载这个类
     * 因为按键绑定、音效播放这些只能在客户端做
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        /**
         * 客户端初始化
         * 注册按键事件监听 + 程序退出时的清理钩子
         * 那个 shutdown hook 是为了确保游戏关闭时麦克风和扬声器能正确释放
         * 不然可能会出现麦克风被占用的问题（别问我怎么知道的）
         */
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("SoundGear 客户端已加载");
            // 把 KeyBindings 类注册到事件总线，这样按键检测的 @SubscribeEvent 才能生效
            MinecraftForge.EVENT_BUS.register(KeyBindings.class);
            // 注册 JVM 关闭钩子，游戏退出时自动清理音频资源
            Runtime.getRuntime().addShutdownHook(new Thread(VoiceChatClientHandler::cleanup));
        }

        /**
         * 注册按键映射到 Minecraft
         * 这样按键才会出现在设置 -> 按键绑定 里面
         */
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            KeyBindings.register(event);
        }
    }
}