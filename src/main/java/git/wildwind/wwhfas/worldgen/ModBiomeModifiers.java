package git.wildwind.wwhfas.worldgen;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_CINDER = createKey("add_cinder");
    public static final ResourceKey<BiomeModifier> ADD_EMBER = createKey("add_ember");

    private ModBiomeModifiers() {
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        HolderSet.Named<Biome> overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

        context.register(
                ADD_CINDER,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CINDER)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
        context.register(
                ADD_EMBER,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EMBER)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }

    private static ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, name)
        );
    }
}