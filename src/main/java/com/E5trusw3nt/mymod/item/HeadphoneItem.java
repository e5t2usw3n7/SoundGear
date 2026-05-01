package com.E5trusw3nt.mymod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 耳机物品类
 * 继承了 ArmorItem（盔甲物品），所以它能装备到头盔栏位
 * 
 * 颜色机制是这个模组的核心——同颜色的耳机在同一个"语音频道"里
 * 也就是说你可以通过染料来切换频道，红色耳机的玩家只能听到红色耳机的玩家说话
 * 这个设计灵感其实来自对讲机的频道概念，我觉得还挺酷的
 * 
 * 现在每种颜色都是独立注册的物品，支持工作台染色
 */
public class HeadphoneItem extends ArmorItem {

    /**
     * 该耳机的颜色RGB值
     */
    private final int color;

    /**
     * 构造函数
     * 
     * @param color 该耳机颜色的RGB值（0xRRGGBB格式）
     * @param properties 物品属性
     */
    public HeadphoneItem(int color, Item.Properties properties) {
        super(ModArmorMaterials.HEADPHONES, ArmorItem.Type.HELMET, properties);
        this.color = color;
    }

    /**
     * 获取该耳机的颜色
     * @return 颜色的 RGB 整数值，格式是 0xRRGGBB
     */
    public int getColor() {
        return this.color;
    }

    /**
     * 获取耳机颜色的静态工具方法
     * @param stack 耳机物品栈
     * @return 颜色的 RGB 整数值
     */
    public static int getHeadphoneColor(ItemStack stack) {
        if (stack.getItem() instanceof HeadphoneItem headphone) {
            return headphone.getColor();
        }
        return 0xFFFFFF; // 兜底返回白色
    }

    /**
     * 判断两个耳机是否为相同颜色
     * 这个方法在语音频道匹配的时候用的——只有颜色一样才能互相听到声音
     * 
     * @param stack1 第一个耳机物品栈
     * @param stack2 第二个耳机物品栈
     * @return true 如果颜色相同，false 如果不同或者其中有不是耳机的东西
     */
    public static boolean isSameColor(ItemStack stack1, ItemStack stack2) {
        if (!(stack1.getItem() instanceof HeadphoneItem) || !(stack2.getItem() instanceof HeadphoneItem)) {
            return false;
        }
        return getHeadphoneColor(stack1) == getHeadphoneColor(stack2);
    }

    /**
     * 获取装备槽位
     * 耳机是头盔类型，固定返回 HEAD 槽位
     * 
     * 注意：在创造模式下右键穿戴后，物品栏里还会保留一份副本，
     * 这不是我们的bug，是创造模式本身的机制（创造模式下不消耗物品）。
     * 生存模式下行为完全正常，所以别再纠结这个问题了。
     */
    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    /**
     * 鼠标悬停时显示的额外信息（tooltip）
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int color = getHeadphoneColor(stack);
        String hexColor = String.format("#%06X", color & 0xFFFFFF);
        tooltip.add(Component.literal("§7颜色: " + hexColor));
        tooltip.add(Component.literal("§7按 §eL §7键开启语音频道"));
    }
}