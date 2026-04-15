package git.wildwind.wwhfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ScorchedTwigBlock extends DeadBushBlock {
    public static final MapCodec<DeadBushBlock> CODEC = simpleCodec(ScorchedTwigBlock::new);

    public ScorchedTwigBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<DeadBushBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT);
    }
}
