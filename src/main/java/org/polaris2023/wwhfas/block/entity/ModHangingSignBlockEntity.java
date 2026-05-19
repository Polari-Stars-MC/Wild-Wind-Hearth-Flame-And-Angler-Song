package org.polaris2023.wwhfas.block.entity;

import org.polaris2023.wwhfas.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 模组悬挂告示牌方块实体喵~
 */
public class ModHangingSignBlockEntity extends SignBlockEntity {
	/**
	 * 创建模组悬挂告示牌方块实体喵~
	 *
	 * @param pos 方块位置喵~
	 * @param blockState 方块状态喵~
	 */
	public ModHangingSignBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.HANGING_SIGN.get(), pos, blockState);
	}

	/**
	 * 获取每一行文本的像素高度喵~
	 *
	 * @return 文本行高喵~
	 */
	@Override
	public int getTextLineHeight() {
		return 9;
	}

	/**
	 * 获取单行文本允许的最大像素宽度喵~
	 *
	 * @return 最大文本宽度喵~
	 */
	@Override
	public int getMaxTextLineWidth() {
		return 60;
	}
}
