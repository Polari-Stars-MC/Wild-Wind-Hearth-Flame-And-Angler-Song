package org.polaris2023.wwhfas.block;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 模组地形相关方块注册表喵~
 */
public final class ModTerrainBlocks {
	/**
	 * 地形方块延迟注册器喵~
	 */
	public static final DeferredRegister<Block> BLOCKS =
		DeferredRegister.create(Registries.BLOCK, WildWindMod.MOD_ID);

	/**
	 * 焦草方块喵~
	 */
	public static final DeferredHolder<Block, Block> SCORCHED_GRASS_BLOCK = BLOCKS.register(
		"scorched_grass_block",
		() -> new ScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK))
	);
	/**
	 * 焦土方块喵~
	 */
	public static final DeferredHolder<Block, Block> SCORCHED_DIRT = BLOCKS.register(
		"scorched_dirt",
		() -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT))
	);
	/**
	 * 焦草植株喵~
	 */
	public static final DeferredHolder<Block, Block> SCORCHED_GRASS = BLOCKS.register(
		"scorched_grass",
		() -> new ScorchedGrassPlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))
	);
	/**
	 * 焦枝喵~
	 */
	public static final DeferredHolder<Block, Block> SCORCHED_TWIG = BLOCKS.register(
		"scorched_twig",
		() -> new ScorchedTwigBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH))
	);
	/**
	 * 墙面焦枝喵~
	 */
	public static final DeferredHolder<Block, Block> SCORCHED_TWIG_WALL = BLOCKS.register(
		"scorched_twig_wall",
		() -> new WallScorchedTwigBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH))
	);

	private ModTerrainBlocks() {
	}

	/**
	 * 向模组事件总线注册地形方块喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
	}
}
