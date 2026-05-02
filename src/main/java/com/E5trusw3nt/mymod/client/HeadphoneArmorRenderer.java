package com.E5trusw3nt.mymod.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;

/**
 * 耳机自定义盔甲渲染器
 * 通过 IClientItemExtensions 让 Forge 使用我们的 3D 模型来渲染耳机
 * 而不是使用默认的平面盔甲纹理（那个 texture_not_found 紫黑格子）
 *
 * 纹理路由由 ArmorMaterial.getName() 自动处理：
 * - 白色耳机（WHITE_HEADPHONES）→ white_headphones_layer_1.png（新建模）
 * - 其他颜色（HEADPHONES）→ headphones_layer_1.png
 */
public class HeadphoneArmorRenderer implements IClientItemExtensions {

    // 单例，避免反复创建
    public static final HeadphoneArmorRenderer INSTANCE = new HeadphoneArmorRenderer();

    // 模型实例（懒加载）
    @Nullable
    private HeadphoneModel model;

    @Override
    public HumanoidModel<?> getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (model == null) {
            model = new HeadphoneModel(getBakedModel());
        }
        return model;
    }

    /**
     * 从 EntityModelSet 获取已烘焙的模型部件
     */
    private static ModelPart getBakedModel() {
        var mc = net.minecraft.client.Minecraft.getInstance();
        return mc.getEntityModels().bakeLayer(HeadphoneModel.LAYER_LOCATION);
    }
}
