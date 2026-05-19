package org.polaris2023.wwhfas.datagen;

import org.polaris2023.wwhfas.datagen.provider.ModBiomeModifiers;
import org.polaris2023.wwhfas.datagen.provider.ModConfiguredFeatures;
import org.polaris2023.wwhfas.datagen.provider.ModPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * 提供世界生成动态注册表构建器喵~
 */
public final class ModWorldGenProvider {
	/**
	 * 世界生成相关注册表的构建器喵~
	 */
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
			.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

	private ModWorldGenProvider() {
	}
}