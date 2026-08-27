package org.polaris2023.wild_wind.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.block.entity.ModHangingSignBlockEntity;
import org.polaris2023.wild_wind.block.entity.ModSignBlockEntity;

import java.util.Arrays;
import java.util.function.Function;

/**
 * 注册模组方块实体类型喵~
 */
public final class ModBlockEntities {
	/**
	 * 模组方块实体类型延迟注册器喵~
	 */
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WildWindMod.MOD_ID);

	/**
	 * 模组告示牌方块实体类型喵~
	 */
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> SIGN =
		BLOCK_ENTITY_TYPES.register(
			"sign",
			() -> BlockEntityType.Builder.of(
					ModSignBlockEntity::new, collectBlocks(ModBlocks.WoodSet::sign, ModBlocks.WoodSet::wallSign)
			).build(null)
		);
	/**
	 * 模组悬挂式告示牌方块实体类型喵~
	 */
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN =
		BLOCK_ENTITY_TYPES.register(
			"hanging_sign",
			() -> BlockEntityType.Builder.of(
				ModHangingSignBlockEntity::new,
				collectBlocks(ModBlocks.WoodSet::hangingSign, ModBlocks.WoodSet::wallHangingSign)
			).build(null)
		);

	private ModBlockEntities() {
	}

	/**
	 * 向模组事件总线注册方块实体类型喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		BLOCK_ENTITY_TYPES.register(modBus);
	}

	@SafeVarargs
	private static Block[] collectBlocks(Function<ModBlocks.WoodSet, DeferredHolder<Block, Block>>... accessors) {
		return ModBlocks.WOOD_SETS.stream()
				.flatMap(woodSet -> Arrays.stream(accessors).map(accessor -> accessor.apply(woodSet).get()))
				.toArray(Block[]::new);
	}
}
