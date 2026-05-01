package com.E5trusw3nt.mymod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class HeadphoneItem extends ArmorItem implements DyeableLeatherItem {

    public HeadphoneItem(Item.Properties properties) {
        super(ModArmorMaterials.HEADPHONES, ArmorItem.Type.HELMET, properties);
    }

    /**
     * 默认颜色为白色 (0xFFFFFF)
     */
    public int getDefaultColor() {
        return 0xFFFFFF;
    }

    /**
     * 获取当前耳机颜色的 RGB 值
     */
    public static int getHeadphoneColor(ItemStack stack) {
        if (stack.getItem() instanceof DyeableLeatherItem dyeable) {
            return dyeable.getColor(stack);
        }
        return 0xFFFFFF;
    }

    /**
     * 判断两个耳机是否为相同颜色（用于语音频道匹配）
     */
    public static boolean isSameColor(ItemStack stack1, ItemStack stack2) {
        if (!(stack1.getItem() instanceof HeadphoneItem) || !(stack2.getItem() instanceof HeadphoneItem)) {
            return false;
        }
        return getHeadphoneColor(stack1) == getHeadphoneColor(stack2);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int color = getHeadphoneColor(stack);
        String hexColor = String.format("#%06X", color & 0xFFFFFF);
        tooltip.add(Component.literal("§7颜色: " + hexColor));
        tooltip.add(Component.literal("§7按 §eL §7键开启语音频道"));
    }
}