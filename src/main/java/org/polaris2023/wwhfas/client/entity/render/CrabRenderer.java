package org.polaris2023.wwhfas.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.polaris2023.wwhfas.client.entity.model.CrabModel;
import org.polaris2023.wwhfas.entity.animal.Crab;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * 螃蟹实体渲染器喵~
 */
public class CrabRenderer extends GeoEntityRenderer<Crab> {
	/**
	 * 创建螃蟹实体渲染器喵~
	 *
	 * @param renderManager 实体渲染上下文喵~
	 */
	public CrabRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CrabModel());
	}

	/**
	 * 在正式渲染前调整幼体缩放与贴附朝向喵~
	 *
	 * @param poseStack 当前姿态栈喵~
	 * @param animatable 待渲染的螃蟹实体喵~
	 * @param model 烘焙后的几何模型喵~
	 * @param bufferSource 渲染缓冲来源喵~
	 * @param buffer 顶点消费者喵~
	 * @param isReRender 是否为重复渲染喵~
	 * @param partialTick 插值用局部刻喵~
	 * @param packedLight 打包光照值喵~
	 * @param packedOverlay 打包覆盖层值喵~
	 * @param colour 颜色值喵~
	 */
	@Override
	public void preRender(PoseStack poseStack, Crab animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		if (!isReRender && animatable.isBaby()) {
			poseStack.scale(0.6f, 0.6f, 0.6f);
		}

		poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTick, animatable.clientSidePreModelYRotOffset, animatable.clientSideModelYRotOffset)));
		float xRot = Mth.lerp(partialTick, animatable.clientSidePreModelXRotOffset, animatable.clientSideModelXRotOffset);
		if (xRot != 0.0f) {
			float xRotProgress = xRot / 90.0f;
			switch (animatable.getMotionDirection()) {
				case NORTH -> poseStack.mulPose(Axis.ZN.rotationDegrees(xRot));
				case SOUTH -> poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
				case WEST -> poseStack.mulPose(Axis.XN.rotationDegrees(xRot));
				case EAST -> poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
			}
			float adjustY = -(animatable.getBbHeight() / 2.0f) * xRotProgress;
			float adjustZ = -(animatable.getBbWidth() / 2.0f) * xRotProgress;
			poseStack.translate(0.0f, adjustY, adjustZ);
		}

		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}
}
