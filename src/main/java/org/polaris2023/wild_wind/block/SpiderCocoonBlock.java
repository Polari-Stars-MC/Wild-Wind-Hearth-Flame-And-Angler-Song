package org.polaris2023.wild_wind.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;
import org.polaris2023.wild_wind.registry.ModItems;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 蜘蛛卵方块
 *
 * @author liudongyu
 */
public class SpiderCocoonBlock extends Block implements IShearable {
	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(1, 0, 1, 15, 14, 15),
			Block.box(3, 14, 3, 13, 26, 13)
	);

	/**
	 * 构造蜘蛛卵方块
	 * @param properties 方块属性
	 */
	public SpiderCocoonBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isSolid();
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
		return List.of(new ItemStack(ModItems.SPIDER_COCOON.get()));
	}

	@Override
	public void spawnShearedDrop(Level level, BlockPos pos, ItemStack drop) {
		Block.popResource(level, pos, drop);
	}
}
