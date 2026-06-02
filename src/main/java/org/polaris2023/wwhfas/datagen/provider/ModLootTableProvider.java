package org.polaris2023.wwhfas.datagen.provider;

import org.polaris2023.wwhfas.block.ModTerrainBlocks;
import org.polaris2023.wwhfas.registry.ModBlocks;
import org.polaris2023.wwhfas.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

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
				List.of(new SubProviderEntry(ModLootSubProvider::new, LootContextParamSets.BLOCK)),
				lookupProvider
		);
	}

	private static final class ModLootSubProvider extends BlockLootSubProvider {
		private ModLootSubProvider(HolderLookup.Provider lookupProvider) {
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

			this.add(ModBlocks.REEDS.get(), createShearsOtherDrop(ModItems.REEDS.get()));
			this.add(ModBlocks.CATTAILS.get(), createShearsOtherDrop(ModItems.CATTAILS.get()));
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
