package org.polaris2023.wwhfas.tag;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * 定义模组群系标签喵~
 */
public interface ModBiomeTags {
	/**
	 * 会生成暖色变种螃蟹的群系标签喵~
	 */
	TagKey<Biome> SPAWNS_WARM_VARIANT_CRABS = create("spawns_warm_variant_crabs");
	/**
	 * 会生成冷色变种螃蟹的群系标签喵~
	 */
	TagKey<Biome> SPAWNS_COLD_VARIANT_CRABS = create("spawns_cold_variant_crabs");

	TagKey<Biome> SPAWNS_PIRANHAS = create("spawns_piranha");

	/**
	 * 创建群系标签键喵~
	 *
	 * @param path 标签路径喵~
	 * @return 群系标签键喵~
	 */
	static TagKey<Biome> create(String path) {
		return TagKey.create(Registries.BIOME, WildWindMod.id(path));
	}
}
