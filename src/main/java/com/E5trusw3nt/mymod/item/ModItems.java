package com.E5trusw3nt.mymod.item;

import com.E5trusw3nt.mymod.SoundGearMod;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoundGearMod.MODID);

    @SuppressWarnings("null")
    public static final RegistryObject<Item> HEADPHONES = ITEMS.register("headphones",
            () -> new HeadphoneItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}