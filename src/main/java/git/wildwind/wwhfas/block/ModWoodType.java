package git.wildwind.wwhfas.block;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodType {
    public static final WoodType CINDER = WoodType.register(new WoodType(namespaced("cinder"), ModBlockSetType.CINDER));
    public static final WoodType EMBER = WoodType.register(new WoodType(namespaced("ember"), ModBlockSetType.EMBER));
    public static final WoodType AZALEA = WoodType.register(new WoodType(namespaced("azalea"), ModBlockSetType.AZALEA));

    private ModWoodType() {
    }

    private static String namespaced(String path) {
        return WildWindMod.MOD_ID + ":" + path;
    }
}
