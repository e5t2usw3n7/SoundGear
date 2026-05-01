package com.E5trusw3nt.mymod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * 模组配置类
 * 这个类用来管理模组的配置项，玩家可以在 config 文件夹里找到对应的配置文件然后改
 * 说实话我现在只写了一个配置项，以后想加的话照着这个格式来就行
 * 
 * 用了 Forge 的配置系统（ForgeConfigSpec），是跟别人学的
 * 它会自动生成配置文件，而且支持热重载（改完配置不用重启游戏）
 */
@Mod.EventBusSubscriber(modid = SoundGearMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    /** 配置构建器，用来定义所有配置项的 */
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /**
     * 是否启用耳机物品的开关
     * 如果设成 false，理论上可以禁用耳机（虽然我现在还没写禁用逻辑就是了……以后再补吧）
     * 在配置文件里对应的就是 [enableHeadphones] 这一项
     */
    private static final ForgeConfigSpec.BooleanValue ENABLE_HEADPHONES = BUILDER
            .comment("是否启用耳机物品")
            .define("enableHeadphones", true);

    /**
     * 最终构建出来的配置规格对象
     * 声明为包级可见（没有 public），这样只有 SoundGearMod 能拿到它来注册
     */
    static final ForgeConfigSpec SPEC = BUILDER.build();

    /**
     * 运行时的实际配置值
     * 注意这个是 static 的，因为全局只需要一份
     * 每次配置文件被加载/修改时，onLoad 方法会更新这个值
     */
    public static boolean enableHeadphones;

    /**
     * 配置加载时的回调
     * 当 Forge 加载或重载配置文件时会自动调用这个方法
     * 然后我们把配置文件里的值同步到上面的 static 变量里
     * 这样其他地方直接用 Config.enableHeadphones 就能读到最新值了
     * 
     * @param event 配置事件，其实我们这里没用到它，但 Forge 要求传这个参数
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableHeadphones = ENABLE_HEADPHONES.get();
    }
}