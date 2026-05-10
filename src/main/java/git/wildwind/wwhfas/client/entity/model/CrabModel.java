package git.wildwind.wwhfas.client.entity.model;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.animal.Crab;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CrabModel extends DefaultedEntityGeoModel<Crab> {
    public CrabModel() {
        super(WildWindMod.id("crab"));
    }

    @Override
    public ResourceLocation getTextureResource(Crab animatable) {
        return animatable.getVariant().value().textureLocation();
    }
}
