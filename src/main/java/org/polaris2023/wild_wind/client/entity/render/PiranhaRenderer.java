package org.polaris2023.wild_wind.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.entity.animal.Piranha;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * 食人鱼渲染器
 * @author huimanman
 */
public class PiranhaRenderer extends GeoEntityRenderer<Piranha> {
    public PiranhaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(WildWindMod.id("piranha")));
    }
}
