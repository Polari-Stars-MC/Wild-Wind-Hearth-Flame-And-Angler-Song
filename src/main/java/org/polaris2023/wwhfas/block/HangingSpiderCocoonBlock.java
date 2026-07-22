package org.polaris2023.wwhfas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 悬挂蜘蛛卵方块
 *
 * @author liudongyu
 */
public class HangingSpiderCocoonBlock extends SpiderCocoonBlock {
	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(1, -24, 1, 15, -10, 15),
			Block.box(3, -10, 3, 13, 2, 13)
	);

	/**
	 * 构造悬挂蜘蛛卵方块
	 *
	 * @param properties 方块属性
	 */
	public HangingSpiderCocoonBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.above()).isSolid();
	}
}
