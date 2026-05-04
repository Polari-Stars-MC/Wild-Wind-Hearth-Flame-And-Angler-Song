package git.wildwind.wwhfas.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import git.wildwind.wwhfas.entity.Crab;
import git.wildwind.wwhfas.entity.model.CrabModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrabRenderer extends GeoEntityRenderer<Crab> {
    public CrabRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrabModel());
    }

    @Override
    public void render(Crab crab, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (crab.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(crab, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
