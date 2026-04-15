package git.wildwind.wwhfas.block;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModTerrainBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(Registries.BLOCK, WildWindMod.MOD_ID);

    public static final DeferredHolder<Block, Block> SCORCHED_GRASS_BLOCK = BLOCKS.register(
        "scorched_grass_block",
        () -> new ScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK))
    );
    public static final DeferredHolder<Block, Block> SCORCHED_DIRT = BLOCKS.register(
        "scorched_dirt",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT))
    );
    public static final DeferredHolder<Block, Block> SCORCHED_GRASS = BLOCKS.register(
        "scorched_grass",
        () -> new ScorchedGrassPlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS))
    );
    public static final DeferredHolder<Block, Block> SCORCHED_TWIG = BLOCKS.register(
        "scorched_twig",
        () -> new ScorchedTwigBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH))
    );
    public static final DeferredHolder<Block, Block> SCORCHED_TWIG_WALL = BLOCKS.register(
        "scorched_twig_wall",
        () -> new WallScorchedTwigBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH))
    );
    public static final DeferredHolder<Block, Block> TINY_CACTUS = BLOCKS.register(
        "tiny_cactus",
        () -> new TinyCactusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CACTUS))
    );
    public static final DeferredHolder<Block, Block> FLETCHIING_TABLE = BLOCKS.register(
        "fletchiing_table",
        () -> new FletchingTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FLETCHING_TABLE))
    );

    private ModTerrainBlocks() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }
}
