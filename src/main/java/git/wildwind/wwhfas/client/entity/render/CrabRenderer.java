package git.wildwind.wwhfas.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import git.wildwind.wwhfas.client.entity.model.CrabModel;
import git.wildwind.wwhfas.entity.Crab;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrabRenderer extends GeoEntityRenderer<Crab> {
    public CrabRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrabModel());
    }

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
