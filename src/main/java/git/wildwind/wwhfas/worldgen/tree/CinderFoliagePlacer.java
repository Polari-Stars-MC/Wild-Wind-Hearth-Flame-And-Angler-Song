package git.wildwind.wwhfas.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import git.wildwind.wwhfas.registry.ModFoliagePlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class CinderFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<CinderFoliagePlacer> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    foliagePlacerParts(instance)
                            .apply(instance, CinderFoliagePlacer::new)
            );

    public CinderFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.CINDER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter,
                                 RandomSource random, TreeConfiguration config,
                                 int maxFreeTreeHeight, FoliageAttachment attachment,
                                 int foliageHeight, int foliageRadius, int offset) {
        BlockPos trunkPos = attachment.pos();


    }
    



    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 1;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
