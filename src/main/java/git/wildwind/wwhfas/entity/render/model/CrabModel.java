package git.wildwind.wwhfas.entity.render.model;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CrabModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/entity/crab"), "main");
    private final ModelPart root;
	private final ModelPart leg_left;
	private final ModelPart leg_left_1;
	private final ModelPart leg_left_2;
	private final ModelPart leg_left_3;
	private final ModelPart leg_right;
	private final ModelPart leg_right_1;
	private final ModelPart leg_right_2;
	private final ModelPart leg_right_3;
	private final ModelPart body;
	private final ModelPart group2;
	private final ModelPart left_claw;
	private final ModelPart group;
	private final ModelPart right_claw;

	public CrabModel(ModelPart root) {
		this.root = root.getChild("root");
		this.leg_left = this.root.getChild("leg_left");
		this.leg_left_1 = this.leg_left.getChild("leg_left_1");
		this.leg_left_2 = this.leg_left.getChild("leg_left_2");
		this.leg_left_3 = this.leg_left.getChild("leg_left_3");
		this.leg_right = this.root.getChild("leg_right");
		this.leg_right_1 = this.leg_right.getChild("leg_right_1");
		this.leg_right_2 = this.leg_right.getChild("leg_right_2");
		this.leg_right_3 = this.leg_right.getChild("leg_right_3");
		this.body = this.root.getChild("body");
		this.group2 = this.body.getChild("group2");
		this.left_claw = this.body.getChild("left_claw");
		this.group = this.left_claw.getChild("group");
		this.right_claw = this.body.getChild("right_claw");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leg_left = root.addOrReplaceChild("leg_left", CubeListBuilder.create(), PartPose.offset(4.5355F, -3.4896F, 1.5F));

		PartDefinition leg_left_1 = leg_left.addOrReplaceChild("leg_left_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition cube_r1 = leg_left_1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 25).addBox(-0.0251F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg_left_2 = leg_left.addOrReplaceChild("leg_left_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = leg_left_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 26).addBox(-0.0251F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg_left_3 = leg_left.addOrReplaceChild("leg_left_3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, -1.1781F, 0.0F));

		PartDefinition cube_r3 = leg_left_3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 27).addBox(-0.0251F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leg_right = root.addOrReplaceChild("leg_right", CubeListBuilder.create(), PartPose.offset(-4.5355F, -3.4896F, 1.5F));

		PartDefinition leg_right_1 = leg_right.addOrReplaceChild("leg_right_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition cube_r4 = leg_right_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 28).addBox(-4.9749F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition leg_right_2 = leg_right.addOrReplaceChild("leg_right_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r5 = leg_right_2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(12, 29).addBox(-4.9749F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition leg_right_3 = leg_right.addOrReplaceChild("leg_right_3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 1.1781F, 0.0F));

		PartDefinition cube_r6 = leg_right_3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 29).addBox(-4.9749F, 0.0F, -0.5F, 5.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -2.1302F, -3.75F, 9.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(12, 30).addBox(-3.5F, -3.1302F, -3.75F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(14, 30).addBox(2.5F, -3.1302F, -3.75F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2698F, -0.25F, -0.0873F, 0.0F, 0.0F));

		PartDefinition group2 = body.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = group2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 25).addBox(-1.5F, -1.0793F, -4.2817F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition left_claw = body.addOrReplaceChild("left_claw", CubeListBuilder.create().texOffs(0, 12).addBox(-2.2219F, -3.5F, -7.2591F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.25F, 0.1198F, -3.0F, 0.2822F, 0.5152F, 0.1441F));

		PartDefinition group = left_claw.addOrReplaceChild("group", CubeListBuilder.create().texOffs(24, 12).addBox(-2.2319F, -0.51F, -7.2491F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition right_claw = body.addOrReplaceChild("right_claw", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -1.5F, -3.0858F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 1.3698F, -3.25F, 0.0F, -0.6981F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}