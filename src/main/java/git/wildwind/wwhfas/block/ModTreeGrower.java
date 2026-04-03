package git.wildwind.wwhfas.block;

import git.wildwind.wwhfas.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class ModTreeGrower {
    public static final TreeGrower CINDER = new TreeGrower(
        "cinder",
        0.0F,
        Optional.empty(),
        Optional.empty(),
        Optional.of(ModConfiguredFeatures.CINDER),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );

    public static final TreeGrower EMBER = new TreeGrower(
        "ember",
        0.0F,
            Optional.empty(),
            Optional.empty(),
        Optional.of(ModConfiguredFeatures.EMBER),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );

    private ModTreeGrower() {
    }
}
