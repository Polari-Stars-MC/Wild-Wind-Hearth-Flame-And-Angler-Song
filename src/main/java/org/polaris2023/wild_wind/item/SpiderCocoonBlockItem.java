package org.polaris2023.wild_wind.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * 蜘蛛卵方块物品
 *
 * @author liudongyu
 */
public class SpiderCocoonBlockItem extends BlockItem {
	private final Block hangingBlock;

	/**
	 * 构造蜘蛛卵方块物品
	 * @param block 方块
	 * @param hangingBlock 悬挂方块
	 * @param properties 物品属性
	 */
	public SpiderCocoonBlockItem(Block block, Block hangingBlock, Properties properties) {
		super(block, properties);
		this.hangingBlock = hangingBlock;
	}

	@Override @Nullable
	protected BlockState getPlacementState(BlockPlaceContext context) {
		Direction face = context.getClickedFace();
		if(face.getAxis().isHorizontal()) {
			return null;
		}
		if(face.getStepY() < 0) {
			return this.hangingBlock.getStateForPlacement(context);
		}
		return this.getBlock().getStateForPlacement(context);
	}
}
