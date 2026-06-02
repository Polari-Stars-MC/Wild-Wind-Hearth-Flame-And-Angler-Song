package org.polaris2023.wwhfas.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.entity.animal.Piranha;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PiranhaRenderer extends GeoEntityRenderer<Piranha> {
    public PiranhaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<Piranha>(WildWindMod.id("piranha")));
    }
}
