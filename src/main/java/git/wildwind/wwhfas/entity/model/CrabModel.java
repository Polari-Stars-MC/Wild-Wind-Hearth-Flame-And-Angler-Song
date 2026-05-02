package git.wildwind.wwhfas.entity.model;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.Crab;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

// TODO: 变种纹理变化, textures/entity/crab.png为占位符
public class CrabModel extends DefaultedEntityGeoModel<Crab> {
    public CrabModel() {
        super(WildWindMod.id("crab"));
    }
}
