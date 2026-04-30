package git.wildwind.wwhfas.entity.render;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.Crab;
import git.wildwind.wwhfas.entity.render.model.CrabModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrabRenderer extends MobRenderer<Crab, CrabModel<Crab>> {

    public static final ResourceLocation CRAB_COLD = ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/entity/crab/crab_cold.png");
    public static final ResourceLocation CRAB_TEMPERATE = ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/entity/crab/crab_temperate.png");
    public static final ResourceLocation CRAB_WARM =  ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/entity/crab/crab_warm.png");

    public CrabRenderer(EntityRendererProvider.Context context) {
        super(context, new CrabModel<>(context.bakeLayer(CrabModel.LAYER_LOCATION)),0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(Crab entity) {
        Crab.CrabVariant variant = entity.getVariant();
        return switch (variant){
            case COLD -> CRAB_COLD;
            case WARM -> CRAB_WARM;
            default -> CRAB_TEMPERATE;
        };
    }

}
