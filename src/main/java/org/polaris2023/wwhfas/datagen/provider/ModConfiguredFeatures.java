package org.polaris2023.wwhfas.datagen.provider;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.registry.ModBlocks;
import org.polaris2023.wwhfas.worldgen.tree.CinderFoliagePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;

/**
 * 定义模组已配置特征喵~
 */
public final class ModConfiguredFeatures {
	/**
	 * 灵焰木的已配置特征键喵~
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> CINDER = createKey("cinder");
	/**
	 * 焚烬木的已配置特征键喵~
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> EMBER = createKey("ember");
	/**
	 * 芦苇补丁的已配置特征键喵~
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_REEDS = createKey("patch_reeds_in_water");

	private ModConfiguredFeatures() {
	}

	/**
	 * 向引导上下文注册已配置特征喵~
	 *
	 * @param context 已配置特征引导上下文喵~
	 */
	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		context.register(PATCH_REEDS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(
				20,
				6,
				2,
				PlacementUtils.inlinePlaced(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.REEDS.get())),
						BlockPredicateFilter.forPredicate(
								BlockPredicate.allOf(
										BlockPredicate.wouldSurvive(ModBlocks.REEDS.get().defaultBlockState(), BlockPos.ZERO)
								)
						)
				)
		)));

//		context.register(CINDER, new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.CINDER)));
		context.register(EMBER, new ConfiguredFeature<>(Feature.TREE, treeConfiguration(ModBlocks.EMBER)));


		context.register(CINDER, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(ModBlocks.CINDER.log().get().defaultBlockState()),
				new CherryTrunkPlacer(6,1,0,
						ConstantInt.of(1),
						UniformInt.of(2, 4),
						UniformInt.of(-3, -2),
						UniformInt.of(-1, 0)
						),
				BlockStateProvider.simple(ModBlocks.CINDER.leaves().get().defaultBlockState()),
				new CinderFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
				new TwoLayersFeatureSize(1, 0, 2)
		).dirt(BlockStateProvider.simple(Blocks.DIRT.defaultBlockState())).ignoreVines().build()));
	}

	private static TreeConfiguration treeConfiguration(ModBlocks.WoodSet woodSet) {
		return new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(woodSet.log().get().defaultBlockState()),
				new StraightTrunkPlacer(6, 0, 4),
				BlockStateProvider.simple(woodSet.leaves().get().defaultBlockState()),
				new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 4),
				new TwoLayersFeatureSize(1, 0, 1)
		).dirt(BlockStateProvider.simple(Blocks.DIRT.defaultBlockState())).ignoreVines().build();
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, name));
	}
}