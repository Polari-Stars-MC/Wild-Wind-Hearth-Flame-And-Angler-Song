package org.polaris2023.wwhfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.polaris2023.wwhfas.block.ModTerrainBlocks;
import org.polaris2023.wwhfas.registry.ModBlocks;
import org.polaris2023.wwhfas.registry.ModEntities;
import org.polaris2023.wwhfas.registry.ModItems;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 生成模组战利品表数据喵~
 */
public class ModLootTableProvider extends LootTableProvider {
	/**
	 * 创建战利品表提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param lookupProvider 注册表查询提供器喵~
	 */
	public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(
				output,
				Set.of(),
				List.of(
						new SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK),
						new SubProviderEntry(ModEntityLootSubProvider::new, LootContextParamSets.ENTITY)
				),
				lookupProvider
		);
	}

	private static final class ModEntityLootSubProvider extends EntityLootSubProvider {
		ModEntityLootSubProvider(HolderLookup.Provider registries) {
			super(FeatureFlags.DEFAULT_FLAGS, registries);
		}

		@Override
		protected Stream<EntityType<?>> getKnownEntityTypes() {
			return ModEntities.ENTITY_TYPES.getEntries()
					.stream()
					.map(DeferredHolder::get);
		}

		@Override
		public void generate() {
			this.emptyLoot(ModEntities.CRAB);
			this.add(
					ModEntities.PIRANHA.get(),
					LootTable.lootTable()
							.withPool(
									LootPool.lootPool()
											.setRolls(ConstantValue.exactly(1.0F))
											.add(LootItem.lootTableItem(ModItems.PIRANHA).apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot())))
							)
							.withPool(
									LootPool.lootPool()
											.setRolls(ConstantValue.exactly(1.0F))
											.add(LootItem.lootTableItem(Items.BONE_MEAL))
											.when(LootItemRandomChanceCondition.randomChance(0.05F))
							)
							.withPool(
									LootPool.lootPool()
											.setRolls(ConstantValue.exactly(1.0F))
											.add(LootItem.lootTableItem(ModItems.FANG))
											.when(LootItemRandomChanceCondition.randomChance(0.05F))
							)
			);
		}

		private <T extends Entity> void emptyLoot(DeferredHolder<EntityType<?>, EntityType<T>> entityType) {
			this.add(entityType.get(), LootTable.lootTable());
		}
	}

	private static final class ModBlockLootSubProvider extends BlockLootSubProvider {
		private ModBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
		}

		@Override
		protected void generate() {
			for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
				dropSelf(woodSet.log().get());
				dropSelf(woodSet.wood().get());
				dropSelf(woodSet.strippedLog().get());
				dropSelf(woodSet.strippedWood().get());
				dropSelf(woodSet.planks().get());
				add(woodSet.slab().get(), this::createSlabItemTable);
				dropSelf(woodSet.stairs().get());
				dropSelf(woodSet.fence().get());
				dropSelf(woodSet.fenceGate().get());
				add(woodSet.door().get(), this::createDoorTable);
				dropSelf(woodSet.trapdoor().get());
				dropSelf(woodSet.pressurePlate().get());
				dropSelf(woodSet.button().get());
				dropSelf(woodSet.sign().get());
				dropSelf(woodSet.hangingSign().get());
				if (woodSet.hasTreeBlocks()) {
					add(woodSet.leaves().get(), block -> createLeavesDrops(block, woodSet.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));
					dropSelf(woodSet.sapling().get());
					dropPottedContents(woodSet.pottedSapling().get());
				}
			}
			dropSelf(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get());
			dropSelf(ModTerrainBlocks.SCORCHED_DIRT.get());
			dropSelf(ModTerrainBlocks.SCORCHED_GRASS.get());
			dropSelf(ModTerrainBlocks.SCORCHED_TWIG.get());
			dropSelf(ModTerrainBlocks.SCORCHED_TWIG_WALL.get());

			this.add(ModBlocks.REEDS.get(), block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
			this.add(ModBlocks.CATTAILS.get(), block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
		}

		/**
		 * 创建仅在使用剪刀时掉落指定物品的战利品表喵~
		 *
		 * @param drop 掉落物喵~
		 * @return 构建中的战利品表喵~
		 */
		public LootTable.Builder createShearsOtherDrop(ItemLike drop) {
			return LootTable.lootTable()
					.withPool(
							LootPool.lootPool().when(HAS_SHEARS).add(LootItem.lootTableItem(drop))
					);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return Stream.concat(ModBlocks.BLOCKS.getEntries().stream(), ModTerrainBlocks.BLOCKS.getEntries().stream())
				.map(holder -> (Block) holder.get())
				.collect(Collectors.toList());
		}
	}
}
