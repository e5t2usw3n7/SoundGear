package com.E5trusw3nt.mymod.client;

import com.E5trusw3nt.mymod.SoundGearMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * 耳机3D模型类 - 基于最新Blockbench导出适配
 * 使用HumanoidModel体系，耳机几何体放在head子部件中
 * bb_main参考框不渲染
 */
public class HeadphoneModel extends HumanoidModel<LivingEntity> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(SoundGearMod.MODID, "headphone"), "main");

    /** 耳机模型根部件（包含左右耳罩、头带等全部几何体） */
    private final ModelPart headphone;

    public HeadphoneModel(ModelPart root) {
        super(root);
        this.headphone = root.getChild("headphone");
    }

    /**
     * 重写 renderToBuffer 以渲染耳机部件
     * HumanoidModel 默认只渲染 head/body/arms/legs 等标准人体部件，
     * 我们需要额外渲染自定义的 "headphone" 部件。
     * 同时将耳机部件与模型头的旋转同步，使其跟随玩家头部转动。
     */
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                                int packedLight, int packedOverlay, float red,
                                float green, float blue, float alpha) {
        // 渲染标准人体部件（头、身体、手臂、腿）
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);

        // 同步耳机部件的旋转角度与头部一致，使其跟随玩家视线
        this.headphone.xRot = this.head.xRot;
        this.headphone.yRot = this.head.yRot;
        this.headphone.zRot = this.head.zRot;

        // 渲染耳机3D几何体
        this.headphone.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
    }

    public static LayerDefinition createBodyLayer() {
        // 创建全新的 MeshDefinition，不渲染默认人体部件
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // 空的默认 HumanoidModel 部件（构造函数需要它们存在）
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        // === 耳机模型 - 直接使用Blockbench坐标 ===
        // Blockbench中head中心在Y=24，HumanoidModel的head部件原点在颈部(Y=0)
        // head部件会跟随头部旋转，所有子部件使用Blockbench坐标即可
        PartDefinition headphone = partdefinition.addOrReplaceChild("headphone",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, 10.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, 24.0F, 4.0F));

        // === 左耳罩组 ===
        PartDefinition left = headphone.addOrReplaceChild("left",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 左耳外圈(hexadecagon)
        PartDefinition hexadecagonLeft = left.addOrReplaceChild("hexadecagonLeft",
                CubeListBuilder.create()
                        .texOffs(4, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 9).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 11).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 12).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 4.0F, 0.0F));

        hexadecagonLeft.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create()
                        .texOffs(12, 8).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(4, 11).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(16, 11).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 0).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        hexadecagonLeft.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create()
                        .texOffs(12, 6).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 10).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        hexadecagonLeft.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create()
                        .texOffs(12, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 5).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        hexadecagonLeft.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create()
                        .texOffs(16, 7).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 14).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 左耳罩主体
        PartDefinition cupLeft = left.addOrReplaceChild("cupLeft",
                CubeListBuilder.create()
                        .texOffs(8, 20).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 14).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 6).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 24).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-6.0F, 4.0F, 0.0F));

        cupLeft.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create()
                        .texOffs(24, 10).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(8, 24).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 16).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 8).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        cupLeft.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create()
                        .texOffs(24, 8).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 4).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 12).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 6).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        cupLeft.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create()
                        .texOffs(20, 18).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 10).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        cupLeft.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create()
                        .texOffs(12, 20).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 4).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 左耳垫
        cupLeft.addOrReplaceChild("cupcushionLeft", CubeListBuilder.create()
                        .texOffs(0, 7).addBox(-5.5F, 3.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(6.0F, -4.0F, 0.0F));

        // 左连接杆
        PartDefinition beamLeft = left.addOrReplaceChild("beamLeft",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        beamLeft.addOrReplaceChild("connection3_r1", CubeListBuilder.create()
                        .texOffs(16, 23).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 19).addBox(0.0F, 0.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.2563F, 7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

        beamLeft.addOrReplaceChild("connection_r1", CubeListBuilder.create()
                        .texOffs(8, 7).addBox(0.0F, 0.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, 6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

        // === 右耳罩组 ===
        PartDefinition right = headphone.addOrReplaceChild("right",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 右耳外圈(hexadecagon)
        PartDefinition hexadecagonRight = right.addOrReplaceChild("hexadecagonRight",
                CubeListBuilder.create()
                        .texOffs(0, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 12).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(14, 3).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 4.0F, 0.0F));

        hexadecagonRight.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create()
                        .texOffs(8, 14).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(0, 13).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(0, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        hexadecagonRight.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create()
                        .texOffs(4, 13).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(12, 10).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
                        .texOffs(8, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 15).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        hexadecagonRight.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create()
                        .texOffs(4, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        hexadecagonRight.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create()
                        .texOffs(18, 2).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 13).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 右耳罩主体
        PartDefinition cupRight = right.addOrReplaceChild("cupRight",
                CubeListBuilder.create()
                        .texOffs(4, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 14).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 20).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 4.0F, 0.0F));

        cupRight.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create()
                        .texOffs(20, 24).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 16).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 23).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        cupRight.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create()
                        .texOffs(24, 18).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 12).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        cupRight.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create()
                        .texOffs(4, 23).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 2).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        cupRight.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create()
                        .texOffs(8, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 20).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        // 右耳垫
        cupRight.addOrReplaceChild("cupcushionRight", CubeListBuilder.create()
                        .texOffs(4, 7).addBox(-5.5F, 3.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(6.0F, -4.0F, 0.0F));

        // 右连接杆
        PartDefinition beamRight = right.addOrReplaceChild("beamRight",
                CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        beamRight.addOrReplaceChild("connection4_r1", CubeListBuilder.create()
                        .texOffs(24, 0).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 22).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 0).addBox(0.0F, 0.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.2563F, 7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

        beamRight.addOrReplaceChild("connection_r2", CubeListBuilder.create()
                        .texOffs(10, 3).addBox(0.0F, 0.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, 6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

        // === 头带 ===
        PartDefinition beam = headphone.addOrReplaceChild("beam",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        beam.addOrReplaceChild("beamright_r1", CubeListBuilder.create()
                        .texOffs(0, 3).addBox(-6.0F, 14.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.926F, -4.3839F, 0.0F, 0.0F, 0.0F, 0.48F));

        beam.addOrReplaceChild("beamleft_r1", CubeListBuilder.create()
                        .texOffs(0, 5).addBox(-6.0F, 14.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.1701F, -0.6899F, 0.0F, 0.0F, 0.0F, -0.48F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}