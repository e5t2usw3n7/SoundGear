package com.E5trusw3nt.mymod.client;

import com.E5trusw3nt.mymod.SoundGearMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * 耳机3D模型
 * 基于 Blockbench 5.1.4 导出的 headphone.bbmodel 建模文件
 * 
 * 继承 HumanoidModel 以兼容盔甲渲染系统，
 * 所有耳机几何体作为 head 部件的子部件，随头部自然旋转。
 * 其他身体部件（body, arms, legs）均为空，不渲染。
 */
public class HeadphoneModel extends HumanoidModel<LivingEntity> {

    /** 模型层位置，用于注册和烘焙 */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(SoundGearMod.MODID, "headphone"), "main");

    /**
     * 构造函数
     * @param root 已烘焙的模型根部件
     */
    public HeadphoneModel(ModelPart root) {
        super(root);
    }

    /**
     * 创建身体图层定义（含耳机几何体）
     * 所有标准 HumanoidModel 部件均为空占位，
     * 耳机部件作为 head 的子部件，自动跟随头部旋转。
     * 
     * @return 图层定义
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // ===== 标准 HumanoidModel 部件（均为空占位） =====
        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("hat",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // ===== 耳机几何体（head 的子部件） =====
        // 耳机根节点（居中在头部，作为 head 的子部件自动跟随头部旋转）
        PartDefinition headphone = head.addOrReplaceChild("headphone",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 顶部头梁
        headphone.addOrReplaceChild("topBeam",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -11.5F, -1.0F, 6.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // ===== 左侧耳罩 =====
        PartDefinition left = headphone.addOrReplaceChild("left",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 左耳罩 - 十六边形装饰环
        PartDefinition hexadecagonLeft = left.addOrReplaceChild("hexadecagonLeft",
                CubeListBuilder.create()
                        .texOffs(4, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 9).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 11).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 12).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -4.0F, 0.0F));

        PartDefinition hexadecagon_r1 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r1",
                CubeListBuilder.create()
                        .texOffs(12, 8).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(4, 11).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(16, 11).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 0).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r2 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r2",
                CubeListBuilder.create()
                        .texOffs(12, 6).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 10).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r3 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r3",
                CubeListBuilder.create()
                        .texOffs(12, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 5).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r4 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r4",
                CubeListBuilder.create()
                        .texOffs(16, 7).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 14).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 左耳杯
        PartDefinition cupLeft = left.addOrReplaceChild("cupLeft",
                CubeListBuilder.create()
                        .texOffs(8, 20).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 14).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 6).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 24).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-6.0F, -4.0F, 0.0F));

        PartDefinition hexadecagon_r5 = cupLeft.addOrReplaceChild("hexadecagon_r5",
                CubeListBuilder.create()
                        .texOffs(24, 10).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(8, 24).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 16).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 8).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r6 = cupLeft.addOrReplaceChild("hexadecagon_r6",
                CubeListBuilder.create()
                        .texOffs(24, 8).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 4).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 12).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 6).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r7 = cupLeft.addOrReplaceChild("hexadecagon_r7",
                CubeListBuilder.create()
                        .texOffs(20, 18).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 10).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r8 = cupLeft.addOrReplaceChild("hexadecagon_r8",
                CubeListBuilder.create()
                        .texOffs(12, 20).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 4).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 左耳罩耳垫
        cupLeft.addOrReplaceChild("cupcushionLeft",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(-5.5F, -5.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(6.0F, 4.0F, 0.0F));

        // 左侧连接架
        PartDefinition beamLeft = left.addOrReplaceChild("beamLeft",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition connection3_r1 = beamLeft.addOrReplaceChild("connection3_r1",
                CubeListBuilder.create()
                        .texOffs(16, 23).addBox(0.0F, -2.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 19).addBox(0.0F, -1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.2563F, -7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

        PartDefinition connection_r1 = beamLeft.addOrReplaceChild("connection_r1",
                CubeListBuilder.create().texOffs(8, 7)
                        .addBox(0.0F, -2.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, -6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

        // ===== 右侧耳罩 =====
        PartDefinition right = headphone.addOrReplaceChild("right",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 右耳罩 - 十六边形装饰环
        PartDefinition hexadecagonRight = right.addOrReplaceChild("hexadecagonRight",
                CubeListBuilder.create()
                        .texOffs(0, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 12).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(14, 3).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -4.0F, 0.0F));

        PartDefinition hexadecagon_r9 = hexadecagonRight.addOrReplaceChild("hexadecagon_r9",
                CubeListBuilder.create()
                        .texOffs(8, 14).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(0, 13).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(0, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r10 = hexadecagonRight.addOrReplaceChild("hexadecagon_r10",
                CubeListBuilder.create()
                        .texOffs(4, 13).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(12, 10).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 15).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r11 = hexadecagonRight.addOrReplaceChild("hexadecagon_r11",
                CubeListBuilder.create()
                        .texOffs(4, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r12 = hexadecagonRight.addOrReplaceChild("hexadecagon_r12",
                CubeListBuilder.create()
                        .texOffs(18, 2).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 13).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 右耳杯
        PartDefinition cupRight = right.addOrReplaceChild("cupRight",
                CubeListBuilder.create()
                        .texOffs(4, 21).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 22).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 14).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 20).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, -4.0F, 0.0F));

        PartDefinition hexadecagon_r13 = cupRight.addOrReplaceChild("hexadecagon_r13",
                CubeListBuilder.create()
                        .texOffs(20, 24).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 16).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 23).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 21).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r14 = cupRight.addOrReplaceChild("hexadecagon_r14",
                CubeListBuilder.create()
                        .texOffs(24, 18).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 12).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 22).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 21).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r15 = cupRight.addOrReplaceChild("hexadecagon_r15",
                CubeListBuilder.create()
                        .texOffs(4, 23).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 2).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition hexadecagon_r16 = cupRight.addOrReplaceChild("hexadecagon_r16",
                CubeListBuilder.create()
                        .texOffs(8, 22).addBox(0.0F, -0.6022F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 20).addBox(0.0F, -0.6022F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 右耳罩耳垫
        cupRight.addOrReplaceChild("cupcushionRight",
                CubeListBuilder.create().texOffs(4, 7)
                        .addBox(-5.5F, -5.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(6.0F, 4.0F, 0.0F));

        // 右侧连接架
        PartDefinition beamRight = right.addOrReplaceChild("beamRight",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition connection4_r1 = beamRight.addOrReplaceChild("connection4_r1",
                CubeListBuilder.create()
                        .texOffs(24, 0).addBox(0.0F, -2.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 22).addBox(0.0F, -1.0F, -0.8978F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 0).addBox(0.0F, -1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.2563F, -7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

        PartDefinition connection_r2 = beamRight.addOrReplaceChild("connection_r2",
                CubeListBuilder.create().texOffs(10, 3)
                        .addBox(0.0F, -2.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, -6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

        // ===== 头梁 =====
        PartDefinition beam = headphone.addOrReplaceChild("beam",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition beamright_r1 = beam.addOrReplaceChild("beamright_r1",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(-6.0F, -15.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.926F, 4.3839F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition beamleft_r1 = beam.addOrReplaceChild("beamleft_r1",
                CubeListBuilder.create().texOffs(0, 5)
                        .addBox(-6.0F, -15.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.1701F, 0.6899F, 0.0F, 0.0F, 0.0F, -0.48F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    /**
     * 覆盖渲染方法，只渲染 head 部件（含耳机几何体）
     * 其他身体部件均为空，不渲染
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                                int packedLight, int packedOverlay,
                                float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
