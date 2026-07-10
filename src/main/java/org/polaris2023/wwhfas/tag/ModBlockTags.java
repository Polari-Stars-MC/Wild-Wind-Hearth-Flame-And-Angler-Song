package org.polaris2023.wwhfas.tag;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * 定义模组方块标签喵~
 */
public interface ModBlockTags {
	/**
	 * 螃蟹可在其上生成的方块标签喵~
	 */
	TagKey<Block> CRAB_SPAWNABLE_ON = BlockTags.create(WildWindMod.id("carb_spawnable_on"));
	/**
	 * 螃蟹可在水下地面生成的方块标签喵~
	 */
	TagKey<Block> CARB_SPAWNABLE_IN_WATER_GROUND = BlockTags.create(WildWindMod.id("carb_spawnable_in_water_ground"));
	/**
	 * 螃蟹偏好游荡的方块标签喵~
	 */
	TagKey<Block> CRAB_PREFERRED_WANDER_BLOCKS = BlockTags.create(WildWindMod.id("crab_preferred_wander_blocks"));

	/**
	 * 蜘蛛卵
	 */
	TagKey<Block> SPIDER_COCOONS = BlockTags.create(WildWindMod.id("spider_cocoons"));
}
