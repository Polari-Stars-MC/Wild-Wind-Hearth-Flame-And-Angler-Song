package git.wildwind.wwhfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.util.RandomSource;

public class ScorchedGrassBlock extends GrassBlock {
    public static final MapCodec<GrassBlock> CODEC = simpleCodec(ScorchedGrassBlock::new);

    public ScorchedGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<GrassBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canRemainScorchedGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) {
                return;
            }
            level.setBlockAndUpdate(pos, ModTerrainBlocks.SCORCHED_DIRT.get().defaultBlockState());
        }
    }

    private static boolean canRemainScorchedGrass(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        }
        int lightBlock = LightEngine.getLightBlockInto(
            level,
            state,
            pos,
            aboveState,
            abovePos,
            Direction.UP,
            aboveState.getLightBlock(level, abovePos)
        );
        return lightBlock < level.getMaxLightLevel();
    }
}
