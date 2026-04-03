package git.wildwind.wwhfas.block;

import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodType {
    public static final WoodType CINDER = WoodType.register(new WoodType("cinder", ModBlockSetType.CINDER));
    public static final WoodType EMBER = WoodType.register(new WoodType("ember", ModBlockSetType.EMBER));

    private ModWoodType() {
    }
}
