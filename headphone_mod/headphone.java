// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class headphone<T extends head> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "headphone"), "main");
	private final ModelPart headphone;
	private final ModelPart left;
	private final ModelPart hexadecagonLeft;
	private final ModelPart cupLeft;
	private final ModelPart cupcushionLeft;
	private final ModelPart beamLeft;
	private final ModelPart right;
	private final ModelPart hexadecagonRight;
	private final ModelPart cupRight;
	private final ModelPart cupcushionRight;
	private final ModelPart beamRight;
	private final ModelPart beam;
	private final ModelPart bb_main;

	public headphone(ModelPart root) {
		this.headphone = root.getChild("headphone");
		this.left = this.headphone.getChild("left");
		this.hexadecagonLeft = this.left.getChild("hexadecagonLeft");
		this.cupLeft = this.left.getChild("cupLeft");
		this.cupcushionLeft = this.cupLeft.getChild("cupcushionLeft");
		this.beamLeft = this.left.getChild("beamLeft");
		this.right = this.headphone.getChild("right");
		this.hexadecagonRight = this.right.getChild("hexadecagonRight");
		this.cupRight = this.right.getChild("cupRight");
		this.cupcushionRight = this.cupRight.getChild("cupcushionRight");
		this.beamRight = this.right.getChild("beamRight");
		this.beam = this.headphone.getChild("beam");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition headphone = partdefinition.addOrReplaceChild("headphone", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 10.5F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 24.0F, 4.0F));

		PartDefinition left = headphone.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hexadecagonLeft = left.addOrReplaceChild("hexadecagonLeft", CubeListBuilder.create().texOffs(4, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 9).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(8, 12).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 4.0F, 0.0F));

		PartDefinition hexadecagon_r1 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(12, 8).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(4, 11).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(16, 11).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(12, 6).addBox(0.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(8, 10).addBox(0.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(8, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(12, 16).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 5).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagonLeft.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(16, 7).addBox(0.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(0.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cupLeft = left.addOrReplaceChild("cupLeft", CubeListBuilder.create().texOffs(8, 20).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 14).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 6).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 24).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 4.0F, 0.0F));

		PartDefinition hexadecagon_r5 = cupLeft.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(24, 10).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 24).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 16).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 8).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = cupLeft.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(24, 8).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 4).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 12).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 6).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r7 = cupLeft.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(20, 18).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 10).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = cupLeft.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(12, 20).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 4).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cupcushionLeft = cupLeft.addOrReplaceChild("cupcushionLeft", CubeListBuilder.create().texOffs(0, 7).addBox(-5.5F, 3.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -4.0F, 0.0F));

		PartDefinition beamLeft = left.addOrReplaceChild("beamLeft", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition connection3_r1 = beamLeft.addOrReplaceChild("connection3_r1", CubeListBuilder.create().texOffs(16, 23).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 19).addBox(0.0F, 0.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.2563F, 7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

		PartDefinition connection_r1 = beamLeft.addOrReplaceChild("connection_r1", CubeListBuilder.create().texOffs(8, 7).addBox(0.0F, 0.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

		PartDefinition right = headphone.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hexadecagonRight = right.addOrReplaceChild("hexadecagonRight", CubeListBuilder.create().texOffs(0, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 12).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(14, 3).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 4.0F, 0.0F));

		PartDefinition hexadecagon_r9 = hexadecagonRight.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(8, 14).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagonRight.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(4, 13).addBox(9.0F, 2.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(12, 10).addBox(9.0F, -3.0F, -0.5967F, 1.0F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(8, 18).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 15).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r11 = hexadecagonRight.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(4, 19).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 17).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r12 = hexadecagonRight.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(18, 2).addBox(9.0F, -0.5967F, 2.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 13).addBox(9.0F, -0.5967F, -3.0F, 1.0F, 1.1935F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cupRight = right.addOrReplaceChild("cupRight", CubeListBuilder.create().texOffs(4, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 14).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 20).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 4.0F, 0.0F));

		PartDefinition hexadecagon_r13 = cupRight.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(20, 24).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 16).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r14 = cupRight.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(24, 18).addBox(0.0F, 1.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 12).addBox(0.0F, -2.0F, -0.3978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r15 = cupRight.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(4, 23).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 2).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r16 = cupRight.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(8, 22).addBox(0.0F, -0.3978F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 20).addBox(0.0F, -0.3978F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cupcushionRight = cupRight.addOrReplaceChild("cupcushionRight", CubeListBuilder.create().texOffs(4, 7).addBox(-5.5F, 3.0F, -0.9728F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -4.0F, 0.0F));

		PartDefinition beamRight = right.addOrReplaceChild("beamRight", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition connection4_r1 = beamRight.addOrReplaceChild("connection4_r1", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 22).addBox(0.0F, 1.0F, -0.8978F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(0.0F, 0.0F, -0.8978F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.2563F, 7.7652F, 0.3978F, 0.0F, 0.0F, -0.0873F));

		PartDefinition connection_r2 = beamRight.addOrReplaceChild("connection_r2", CubeListBuilder.create().texOffs(10, 3).addBox(0.0F, 0.0F, 0.1022F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 6.0F, -0.6022F, 0.0F, 0.0F, 0.1309F));

		PartDefinition beam = headphone.addOrReplaceChild("beam", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition beamright_r1 = beam.addOrReplaceChild("beamright_r1", CubeListBuilder.create().texOffs(0, 3).addBox(-6.0F, 14.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.926F, -4.3839F, 0.0F, 0.0F, 0.0F, 0.48F));

		PartDefinition beamleft_r1 = beam.addOrReplaceChild("beamleft_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-6.0F, 14.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.1701F, -0.6899F, 0.0F, 0.0F, 0.0F, -0.48F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-12, -6).addBox(-8.0F, 24.0F, 0.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(head entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		headphone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}