package org.polaris2023.wild_wind.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.polaris2023.wild_wind.WildWindMod;

/**
 * 定义模组群系标签喵~
 */
public interface ModBiomeTags {
	/**
	 * 会生成暖色变种泥沼蟹的群系标签喵~
	 */
	TagKey<Biome> SPAWNS_WARM_VARIANT_MUDCRABS = create("spawns_warm_variant_mudcrabs");
	/**
	 * 会生成冷色变种泥沼蟹的群系标签喵~
	 */
	TagKey<Biome> SPAWNS_COLD_VARIANT_MUDCRABS = create("spawns_cold_variant_mudcrabs");

	TagKey<Biome> SPAWNS_PIRANHAS = create("spawns_piranha");

	TagKey<Biome> HAS_STRUCTURE_RUINED_CHAPEL = create("has_structure/ruined_chapel");

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
