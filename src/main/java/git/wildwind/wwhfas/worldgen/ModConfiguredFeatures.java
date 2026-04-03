package git.wildwind.wwhfas.worldgen;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public final class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CINDER = createKey("cinder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> EMBER = createKey("ember");

    private ModConfiguredFeatures() {
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<net.minecraft.world.level.block.Block> blocks = context.lookup(Registries.BLOCK);

        context.register(CINDER, new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.CINDER)));
        context.register(EMBER, new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.EMBER)));
    }

    private static TreeConfiguration treeConfiguration(ModBlocks.WoodSet woodSet) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(woodSet.log().get().defaultBlockState()),
                new StraightTrunkPlacer(5, 2, 0),
                BlockStateProvider.simple(woodSet.leaves().get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).dirt(BlockStateProvider.simple(Blocks.DIRT.defaultBlockState())).ignoreVines().build();
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, name));
    }
}