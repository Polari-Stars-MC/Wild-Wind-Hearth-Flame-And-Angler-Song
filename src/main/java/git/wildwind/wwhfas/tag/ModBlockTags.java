package git.wildwind.wwhfas.tag;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface ModBlockTags {
    TagKey<Block> CRAB_SPAWNABLE_ON = BlockTags.create(WildWindMod.id("carb_spawnable_on"));
    TagKey<Block> CARB_SPAWNABLE_IN_WATER_GROUND = BlockTags.create(WildWindMod.id("carb_spawnable_in_water_ground"));
    TagKey<Block> CRAB_PREFERRED_WANDER_BLOCKS = BlockTags.create(WildWindMod.id("crab_preferred_wander_blocks"));
}
