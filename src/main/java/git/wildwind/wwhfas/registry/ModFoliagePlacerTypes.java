package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.worldgen.tree.CinderFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModFoliagePlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, WildWindMod.MOD_ID);

    public static final Supplier<FoliagePlacerType<CinderFoliagePlacer>> CINDER =
            FOLIAGE_PLACER_TYPE.register("cinder",
                    () -> new FoliagePlacerType<>(CinderFoliagePlacer.CODEC));

}
