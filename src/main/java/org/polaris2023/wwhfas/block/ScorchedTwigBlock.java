package org.polaris2023.wwhfas.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 焦枝方块喵~
 */
public class ScorchedTwigBlock extends DeadBushBlock {
	/**
	 * 焦枝方块的编解码器喵~
	 */
	public static final MapCodec<DeadBushBlock> CODEC = simpleCodec(ScorchedTwigBlock::new);

	/**
	 * 创建焦枝方块喵~
	 *
	 * @param properties 方块属性喵~
	 */
	public ScorchedTwigBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	/**
	 * 获取焦枝方块的编解码器喵~
	 *
	 * @return 焦枝方块编解码器喵~
	 */
	@Override
	public MapCodec<DeadBushBlock> codec() {
		return CODEC;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT);
	}
}
