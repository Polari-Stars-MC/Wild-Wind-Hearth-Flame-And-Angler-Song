package git.wildwind.wwhfas.block;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ModBlockSetType {
    public static final BlockSetType CINDER = BlockSetType.register(new BlockSetType(namespaced("cinder")));
    public static final BlockSetType EMBER = BlockSetType.register(new BlockSetType(namespaced("ember")));
    public static final BlockSetType AZALEA = BlockSetType.register(new BlockSetType(namespaced("azalea")));

    private ModBlockSetType() {
    }

    private static String namespaced(String path) {
        return WildWindMod.MOD_ID + ":" + path;
    }
}
