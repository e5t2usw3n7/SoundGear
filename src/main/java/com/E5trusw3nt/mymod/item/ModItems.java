package com.E5trusw3nt.mymod.item;

import com.E5trusw3nt.mymod.SoundGearMod;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 物品注册类
 * 这个类负责把所有自定义物品注册到 Forge 的物品注册表里
 * 
 * 用的是 DeferredRegister 机制，简单来说就是：
 * 你不用手动在某个特定时机去注册物品，只需要在这里声明一下
 * Forge 会在合适的时候自动帮你注册
 * 
 * 现在有16种颜色的耳机，每种颜色都是独立注册的物品
 */
public class ModItems {
    /**
     * 物品注册器
     * 第一个参数是物品注册表（ForgeRegistries.ITEMS）
     * 第二个参数是模组ID，所有注册的物品都会带上这个前缀
     * 这样不同模组的物品就不会有命名冲突了
     */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoundGearMod.MODID);

    /**
     * 16种颜色的耳机物品
     * 每种颜色都是独立的物品，支持工作台染色
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Item> WHITE_HEADPHONES = ITEMS.register("white_headphones",
            () -> new HeadphoneItem(0xF9FFFE, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> ORANGE_HEADPHONES = ITEMS.register("orange_headphones",
            () -> new HeadphoneItem(0xF9801D, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> MAGENTA_HEADPHONES = ITEMS.register("magenta_headphones",
            () -> new HeadphoneItem(0xC74EBD, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> LIGHT_BLUE_HEADPHONES = ITEMS.register("light_blue_headphones",
            () -> new HeadphoneItem(0x3AB3DA, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> YELLOW_HEADPHONES = ITEMS.register("yellow_headphones",
            () -> new HeadphoneItem(0xFED83D, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> LIME_HEADPHONES = ITEMS.register("lime_headphones",
            () -> new HeadphoneItem(0x80C71F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> PINK_HEADPHONES = ITEMS.register("pink_headphones",
            () -> new HeadphoneItem(0xF38BAA, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> GRAY_HEADPHONES = ITEMS.register("gray_headphones",
            () -> new HeadphoneItem(0x474F52, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> LIGHT_GRAY_HEADPHONES = ITEMS.register("light_gray_headphones",
            () -> new HeadphoneItem(0x9D9D97, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> CYAN_HEADPHONES = ITEMS.register("cyan_headphones",
            () -> new HeadphoneItem(0x169C9C, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> PURPLE_HEADPHONES = ITEMS.register("purple_headphones",
            () -> new HeadphoneItem(0x8932B8, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> BLUE_HEADPHONES = ITEMS.register("blue_headphones",
            () -> new HeadphoneItem(0x3C44AA, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> BROWN_HEADPHONES = ITEMS.register("brown_headphones",
            () -> new HeadphoneItem(0x835432, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> GREEN_HEADPHONES = ITEMS.register("green_headphones",
            () -> new HeadphoneItem(0x5E7C16, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> RED_HEADPHONES = ITEMS.register("red_headphones",
            () -> new HeadphoneItem(0xB02E26, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> BLACK_HEADPHONES = ITEMS.register("black_headphones",
            () -> new HeadphoneItem(0x1D1D21, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    /**
     * 所有耳机物品的列表，方便遍历
     */
    public static final RegistryObject<Item>[] ALL_HEADPHONES = new RegistryObject[]{
        WHITE_HEADPHONES, ORANGE_HEADPHONES, MAGENTA_HEADPHONES, LIGHT_BLUE_HEADPHONES,
        YELLOW_HEADPHONES, LIME_HEADPHONES, PINK_HEADPHONES, GRAY_HEADPHONES,
        LIGHT_GRAY_HEADPHONES, CYAN_HEADPHONES, PURPLE_HEADPHONES, BLUE_HEADPHONES,
        BROWN_HEADPHONES, GREEN_HEADPHONES, RED_HEADPHONES, BLACK_HEADPHONES
    };

    /**
     * 注册物品到事件总线
     * 这个方法在 SoundGearMod 的构造函数里被调用
     * 不调这个的话，上面的 ITEMS 注册器就不会工作，啥也注册不了
     * 
     * @param eventBus 模组事件总线
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}