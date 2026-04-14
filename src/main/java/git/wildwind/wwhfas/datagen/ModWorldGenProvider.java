package git.wildwind.wwhfas.datagen;

import git.wildwind.wwhfas.worldgen.ModBiomeModifiers;
import git.wildwind.wwhfas.worldgen.ModConfiguredFeatures;
import git.wildwind.wwhfas.worldgen.ModPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModWorldGenProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    private ModWorldGenProvider() {
    }
}