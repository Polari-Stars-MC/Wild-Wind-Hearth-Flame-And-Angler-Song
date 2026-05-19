package git.wildwind.wwhfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 焦草植株方块喵~
 */
public class ScorchedGrassPlantBlock extends TallGrassBlock {
    /**
     * 焦草植株方块的编解码器喵~
     */
    public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(ScorchedGrassPlantBlock::new);

    /**
     * 创建焦草植株方块喵~
     *
     * @param properties 方块属性喵~
     */
    public ScorchedGrassPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * 获取焦草植株方块的编解码器喵~
     *
     * @return 焦草植株方块编解码器喵~
     */
    @Override
    public MapCodec<TallGrassBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT);
    }
}
