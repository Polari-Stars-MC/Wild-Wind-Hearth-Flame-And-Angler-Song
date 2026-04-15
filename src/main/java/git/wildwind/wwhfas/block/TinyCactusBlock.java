package git.wildwind.wwhfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TinyCactusBlock extends CactusBlock {
    public static final MapCodec<CactusBlock> CODEC = simpleCodec(TinyCactusBlock::new);

    public TinyCactusBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<CactusBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos sidePos = pos.relative(direction);
            BlockState sideState = level.getBlockState(sidePos);
            if (sideState.isSolid() || level.getFluidState(sidePos).is(FluidTags.LAVA)) {
                return false;
            }
        }

        BlockState belowState = level.getBlockState(pos.below());
        return (belowState.is(this) || belowState.is(BlockTags.SAND) || belowState.is(Blocks.CACTUS))
            && !level.getBlockState(pos.above()).liquid();
    }
}
