package org.polaris2023.wwhfas.client.entity.render.layer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.entity.monster.Spiderling;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SpiderlingEyesRenderLayer extends AutoGlowingGeoLayer<Spiderling> {
    private static final RenderType EYES = RenderType.eyes(WildWindMod.id("textures/entity/spiderling_eyes.png"));

    public SpiderlingEyesRenderLayer(GeoRenderer<Spiderling> renderer) {
        super(renderer);
    }

    @Override
    protected @Nullable RenderType getRenderType(Spiderling animatable, @Nullable MultiBufferSource bufferSource) {
        return EYES;
    }
}
