package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.registry.ModBlocks;

import java.util.List;

/**
 * 定义模组已放置特征喵~
 */
public final class ModPlacedFeatures {
	/**
	 * 灵焰木的已放置特征键喵~
	 */
	public static final ResourceKey<PlacedFeature> CINDER = createKey("cinder");
	/**
	 * 焚烬木的已放置特征键喵~
	 */
	public static final ResourceKey<PlacedFeature> EMBER = createKey("ember");
	/**
	 * 芦苇补丁的已放置特征键喵~
	 */
	public static final ResourceKey<PlacedFeature> PATCH_REEDS = createKey("patch_reeds");

	public static final ResourceKey<PlacedFeature> PATCH_CATTAILS = createKey("patch_cattails");

	public static final ResourceKey<PlacedFeature> GRASS_SWAMP_BONEMEAL = createKey("grass_swamp_bonemeal");

	private ModPlacedFeatures() {
	}

	/**
	 * 向引导上下文注册已放置特征喵~
	 *
	 * @param context 已放置特征引导上下文喵~
	 */
	public static void bootstrap(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

		context.register(CINDER, new PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.CINDER),
				treePlacement(1)
		));
		context.register(EMBER, new PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.EMBER),
				treePlacement(1)
		));

		context.register(PATCH_REEDS, new PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.PATCH_REEDS),
				List.of(
						BiomeFilter.biome(),
						CountPlacement.of(3),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
						BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER))
				)
		));

		context.register(PATCH_CATTAILS, new PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.PATCH_CATTAILS),
				List.of(
						BiomeFilter.biome(),
						CountPlacement.of(3),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
						BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER))
				)
		));

		context.register(GRASS_SWAMP_BONEMEAL, new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.RANDOM_BOOLEAN_SELECTOR,
						new RandomBooleanFeatureConfiguration(
								Holder.direct(
										new PlacedFeature(
												Holder.direct(new ConfiguredFeature<>(
														Feature.RANDOM_BOOLEAN_SELECTOR,
														new RandomBooleanFeatureConfiguration(
																Holder.direct(new PlacedFeature(
																		Holder.direct(new ConfiguredFeature<>(
																				Feature.SIMPLE_BLOCK,
																				new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.REEDS.get()))
																		)), List.of()
																)),
																Holder.direct(new PlacedFeature(
																		Holder.direct(new ConfiguredFeature<>(
																				Feature.SIMPLE_BLOCK,
																				new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CATTAILS.get()))
																		)), List.of()
																))
														)
												)), List.of()
										)
								),
								placedFeatures.getOrThrow(VegetationPlacements.GRASS_BONEMEAL)
						)
				)), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))
		));
	}

	private static List<PlacementModifier> treePlacement(int count) {
		return List.of(
				CountPlacement.of(count),
				InSquarePlacement.spread(),
				SurfaceWaterDepthFilter.forMaxDepth(0),
				HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
				BiomeFilter.biome()
		);
	}

	private static ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, name));
	}
}