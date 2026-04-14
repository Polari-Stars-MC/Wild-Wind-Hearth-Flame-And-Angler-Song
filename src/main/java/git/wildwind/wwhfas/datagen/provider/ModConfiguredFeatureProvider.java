package git.wildwind.wwhfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import com.mojang.serialization.Codec;
import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Generates dynamic registry overrides that need to live under the vanilla namespace.
 */
public final class ModConfiguredFeatureProvider extends JsonCodecProvider<ConfiguredFeature<?, ?>> {
    private static final Codec<ConfiguredFeature<?, ?>> CODEC = ConfiguredFeature.DIRECT_CODEC;

    public ModConfiguredFeatureProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                output,
                PackOutput.Target.DATA_PACK,
                "worldgen/configured_feature",
                PackType.SERVER_DATA,
                CODEC,
                lookupProvider,
                WildWindMod.MOD_ID,
                existingFileHelper
        );
    }

    @Override
    protected void gather() {
        unconditional(ResourceLocation.withDefaultNamespace("azalea_tree"), azaleaTree());
    }

    private static ConfiguredFeature<?, ?> azaleaTree() {
        TreeConfiguration configuration = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.AZALEA.log().get().defaultBlockState()),
                new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)),
                new WeightedStateProvider(
                        SimpleWeightedRandomList.<BlockState>builder()
                                .add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3)
                                .add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)
                ),
                new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
                new TwoLayersFeatureSize(1, 0, 1)
        ).dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT)).forceDirt().build();

        return new ConfiguredFeature<>(Feature.TREE, configuration);
    }
}
