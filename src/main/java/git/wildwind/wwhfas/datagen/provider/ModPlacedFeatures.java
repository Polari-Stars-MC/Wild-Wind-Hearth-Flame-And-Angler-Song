package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

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
     * 地表芦苇补丁的已放置特征键喵~
     */
    public static final ResourceKey<PlacedFeature> PATCH_REEDS_SURFACE = createKey("patch_reeds_surface");
    /**
     * 水中芦苇补丁的已放置特征键喵~
     */
    public static final ResourceKey<PlacedFeature> PATCH_REEDS_IN_WATER = createKey("patch_reeds_in_water");

    private ModPlacedFeatures() {
    }

    /**
     * 向引导上下文注册已放置特征喵~
     *
     * @param context 已放置特征引导上下文喵~
     */
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(CINDER, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.CINDER),
                treePlacement(1)
        ));
        context.register(EMBER, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.EMBER),
                treePlacement(1)
        ));

        context.register(PATCH_REEDS_SURFACE, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PATCH_REEDS),
                List.of(
                        BiomeFilter.biome(),
						CountPlacement.of(16),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
                )
        ));

        context.register(PATCH_REEDS_IN_WATER, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PATCH_REEDS),
                List.of(
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER)),
						CountPlacement.of(16),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG)
                )
        ));
    }

    private static List<net.minecraft.world.level.levelgen.placement.PlacementModifier> treePlacement(int count) {
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