package org.polaris2023.wwhfas.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.client.entity.render.layer.SpiderlingEyesRenderLayer;
import org.polaris2023.wwhfas.entity.monster.Spiderling;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpiderlingRenderer extends GeoEntityRenderer<Spiderling> {
    public SpiderlingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(WildWindMod.id("spiderling")));
        this.addRenderLayer(new SpiderlingEyesRenderLayer(this));
    }
}
