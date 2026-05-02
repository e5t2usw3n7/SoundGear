package com.E5trusw3nt.mymod.item;

import com.E5trusw3nt.mymod.SoundGearMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * 自定义护甲材质枚举
 * 
 * 这个类定义了耳机的护甲属性。Minecraft 里的每种盔甲都需要一个材质（ArmorMaterial）
 * 材质决定了护甲的耐久度、防御值、附魔能力、穿戴音效等
 * 
 * 用枚举来实现是因为 ArmorMaterial 接口要求必须这样写（Forge 的接口设计）
 * 虽然看起来有点复杂，但其实就是一堆 getter 方法而已
 * 
 * 耳机的属性设计思路：
 * - 耐久度：5倍（和铁甲差不多，不算太脆）
 * - 防御值：头盔位为 1（0.5个护甲值，很薄的防护）
 * - 附魔能力：25（和铁甲一样，可以正常附魔）
 * - 穿戴音效：皮革装备的音效（比较符合耳机的质感）
 * - 韧性和击退抗性：都是0（耳机嘛，又不是钻石甲）
 * - 修复材料：空的（耳机目前不能修复，以后再说）
 */
public enum ModArmorMaterials implements ArmorMaterial {
    /**
     * 耳机材质
     * 
     * 参数详解：
     * 1. name: "soundgear:headphones" - 材质名称，用于模型/纹理文件的查找
     * 2. durabilityMultiplier: 5 - 耐久倍数，乘以各槽位的基础耐久值
     * 3. slotProtections: {0, 0, 0, 1} - 各槽位防御值（头盔=1，其他=0）
     *    这里只有最后一个位置（头盔，索引3）有值1，其他都是0
     *    1点防御值 = 0.5个护甲图标（半个盔甲）
     * 4. enchantmentValue: 25 - 附魔能力值，越高附魔台出的附魔越好
     *    铁甲是25，钻石甲是33，金甲也是25
     * 5. sound: 皮革装备音效 - 穿戴时发出的声音
     * 6. toughness: 0.0F - 韧性（减少穿甲伤害，钻石甲有这个）
     * 7. knockbackResistance: 0.0F - 击退抗性（下界合金甲有这个）
     * 8. repairIngredient: Ingredient.EMPTY - 修复材料（空的表示不能修复）
     */
    HEADPHONES(SoundGearMod.MODID + ":headphones", 5, new int[]{0, 0, 0, 1}, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.IRON_INGOT)),

    /**
     * 白色耳机专用材质
     * 使用 headphone_mod 文件夹中的新建模纹理
     * 纹理路径由名称决定：soundgear:white_headphones → white_headphones_layer_1.png
     * 属性与 HEADPHONES 完全一致，仅材质名称不同以路由到不同纹理
     */
    WHITE_HEADPHONES(SoundGearMod.MODID + ":white_headphones", 5, new int[]{0, 0, 0, 1}, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.IRON_INGOT));

    /**
     * 各槽位的基础耐久值
     * Minecraft 的盔甲有4个槽位：头盔、胸甲、护腿、靴子
     * 这些值会被 durabilityMultiplier 相乘得到实际耐久
     * 对于耳机来说只有头盔位有效：11 * 5 = 55 点耐久
     */
    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    /** 材质名称，用于资源文件查找 */
    private final String name;

    /** 耐久倍数 */
    private final int durabilityMultiplier;

    /** 各槽位的防御值数组 */
    private final int[] slotProtections;

    /** 附魔能力值 */
    private final int enchantmentValue;

    /** 穿戴音效 */
    private final SoundEvent sound;

    /** 韧性值 */
    private final float toughness;

    /** 击退抗性 */
    private final float knockbackResistance;

    /** 修复材料（懒加载，只在需要时才计算） */
    private final Supplier<Ingredient> repairIngredient;

    /**
     * 构造函数
     * 所有参数都是从枚举常量那里传进来的，不需要手动调用
     */
    ModArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections,
                      int enchantmentValue, SoundEvent sound, float toughness,
                      float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    /**
     * 获取指定盔甲类型的耐久度
     * 公式：基础耐久值 * 耐久倍数
     * 
     * @param type 盔甲类型（头盔/胸甲/护腿/靴子）
     * @return 耐久度
     */
    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    /**
     * 获取指定盔甲类型的防御值
     * 耳机只有头盔位有防御值（1点 = 0.5个护甲）
     * 
     * @param type 盔甲类型
     * @return 防御值
     */
    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.slotProtections[type.getSlot().getIndex()];
    }

    /**
     * 获取附魔能力值
     * 值越高，在附魔台上能获得的附魔等级越好
     * 耳机的25点和铁甲一样，算是中等水平
     * 
     * @return 附魔能力值
     */
    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    /**
     * 获取穿戴时的音效
     * 耳机用的是皮革装备的音效，比较轻柔
     * 
     * @return 穿戴音效
     */
    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    /**
     * 获取修复材料
     * 耳机目前设为空（Ingredient.EMPTY），表示不能在铁砧上修复
     * 如果以后想让玩家能修复耳机，可以把这个改成某个物品
     * 
     * @return 修复材料
     */
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get(); // LazyLoadedValue 已弃用，改用 Supplier
    }

    /**
     * 获取材质名称
     * 这个名称会被用来查找对应的模型和纹理文件
     * 格式是 "soundgear:headphones"，对应资源文件路径会用到
     * 
     * @return 材质名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 获取韧性值
     * 韧性可以减少来自高伤害攻击的穿甲效果
     * 耳机没有韧性（0.0），只有钻石甲和下界合金甲才有
     * 
     * @return 韧性值
     */
    @Override
    public float getToughness() {
        return this.toughness;
    }

    /**
     * 获取击退抗性
     * 击退抗性可以减少被攻击时的击退距离
     * 耳机没有击退抗性（0.0），只有下界合金甲才有
     * 
     * @return 击退抗性（0.0到1.0之间）
     */
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}