package org.polaris2023.wild_wind.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.polaris2023.wild_wind.block.entity.ModSignBlockEntity;

import java.util.function.Supplier;

/**
 * 模组直立告示牌方块喵~
 */
public class ModStandingSignBlock extends StandingSignBlock {
	private final Supplier<BlockEntityType<? extends SignBlockEntity>> blockEntityType;

	/**
	 * 创建模组直立告示牌方块喵~
	 *
	 * @param woodType 木材类型喵~
	 * @param properties 方块属性喵~
	 * @param blockEntityType 告示牌方块实体类型提供器喵~
	 */
	public ModStandingSignBlock(
			WoodType woodType,
			Properties properties,
			Supplier<BlockEntityType<? extends SignBlockEntity>> blockEntityType
	) {
		super(woodType, properties);
		this.blockEntityType = blockEntityType;
	}

	/**
	 * 创建对应的直立告示牌方块实体喵~
	 *
	 * @param pos 方块位置喵~
	 * @param state 方块状态喵~
	 * @return 直立告示牌方块实体喵~
	 */
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ModSignBlockEntity(pos, state);
	}

	/**
	 * 获取告示牌方块实体的刻更新器喵~
	 *
	 * @param level 当前世界喵~
	 * @param state 当前方块状态喵~
	 * @param type 当前方块实体类型喵~
	 * @param <T> 方块实体类型喵~
	 * @return 告示牌刻更新器喵~
	 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, this.blockEntityType.get(), SignBlockEntity::tick);
	}
}
