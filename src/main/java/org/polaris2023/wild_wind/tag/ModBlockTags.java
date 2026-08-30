package org.polaris2023.wild_wind.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wild_wind.WildWindMod;

/**
 * 定义模组方块标签喵~
 */
public interface ModBlockTags {
	/**
	 * 泥沼蟹可在其上生成的方块标签喵~
	 */
	TagKey<Block> MUDCRAB_SPAWNABLE_ON = BlockTags.create(WildWindMod.id("mudcrab_spawnable_on"));
	/**
	 * 泥沼蟹可在水下地面生成的方块标签喵~
	 */
	TagKey<Block> MUDCRAB_SPAWNABLE_IN_WATER_GROUND = BlockTags.create(WildWindMod.id("mudcrab_spawnable_in_water_ground"));
	/**
	 * 泥沼蟹偏好游荡的方块标签喵~
	 */
	TagKey<Block> MUDCRAB_PREFERRED_WANDER_BLOCKS = BlockTags.create(WildWindMod.id("mudcrab_preferred_wander_blocks"));

	/**
	 * 蜘蛛卵
	 */
	TagKey<Block> SPIDER_COCOONS = BlockTags.create(WildWindMod.id("spider_cocoons"));
}
