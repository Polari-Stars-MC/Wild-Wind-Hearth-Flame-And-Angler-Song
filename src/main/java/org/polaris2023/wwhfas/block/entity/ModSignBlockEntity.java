package org.polaris2023.wwhfas.block.entity;

import org.polaris2023.wwhfas.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 模组告示牌方块实体喵~
 */
public class ModSignBlockEntity extends SignBlockEntity {
	/**
	 * 创建模组告示牌方块实体喵~
	 *
	 * @param pos 方块位置喵~
	 * @param blockState 方块状态喵~
	 */
	public ModSignBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.SIGN.get(), pos, blockState);
	}
}
