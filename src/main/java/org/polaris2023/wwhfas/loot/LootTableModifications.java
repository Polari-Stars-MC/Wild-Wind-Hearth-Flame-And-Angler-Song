package org.polaris2023.wwhfas.loot;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;
import org.polaris2023.wwhfas.registry.ModItems;
import org.polaris2023.wwhfas.registry.ModPotions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 战利品表修改入口，负责在加载时向原版战利品表附加模组内容喵~
 */
public class LootTableModifications {
	private static final String MAIN_LOOT_POOL_NAME = "main";
	private static final String LOOT_POOL_NAME_PREFIX = "pool";
	@Nullable
	private static Object2ObjectOpenHashMap<ResourceKey<LootTable>, BiConsumer<LootTable, HolderLookup.Provider>> modifications = new Object2ObjectOpenHashMap<>();

	static {
		// 修改北极熊
		registerModification(EntityType.POLAR_BEAR.getDefaultLootTable(), addEntryToByIndexPool(0, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.PIRANHA) // 添加模组鱼
								.apply(SmeltItemFunction.smelted().when(shouldSmeltLoot(registries)))
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F)))
								.build()
		}));

		// 修改村庄渔夫箱
		registerModification(BuiltInLootTables.VILLAGE_FISHER, addEntryToByIndexPool(0, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.PIRANHA) // 添加模组鱼
								.setWeight(1)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.build()
		}));

		// 修改奖励箱
		registerModification(BuiltInLootTables.SPAWN_BONUS_CHEST, addEntryToByIndexPool(2, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.PIRANHA) // 添加模组鱼
								.setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
								.build()
		}));

		// 修改埋藏的宝藏
		registerModification(BuiltInLootTables.BURIED_TREASURE, addEntryToByIndexPool(4, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.COOKED_PIRANHA) // 添加模组鱼
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
								.build()
		}));

		// 修改钓鱼渔获中的鱼
		registerModification(BuiltInLootTables.FISHING_FISH, addEntryToByIndexPool(0, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.PIRANHA) // 添加模组鱼
								.setWeight(10)
								.build()
		}));

		// 修改制箭师村庄英雄礼物
		registerModification(BuiltInLootTables.FLETCHER_GIFT, addEntryToByIndexPool(0, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(Items.TIPPED_ARROW) // 添加药水箭
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(SetPotionFunction.setPotion(ModPotions.REACH))
								.build()
		}));

		// 修改渔夫村庄英雄礼物
		registerModification(BuiltInLootTables.FISHERMAN_GIFT, addEntryToByIndexPool(0, registries ->
                new LootPoolEntryContainer[]{
						LootItem.lootTableItem(ModItems.PIRANHA) // 添加模组鱼
								.build()
		}));
	}

	private static BiConsumer<LootTable, HolderLookup.Provider> addEntryToByIndexPool(int index, Function<HolderLookup.Provider, LootPoolEntryContainer[]> entriesFunction) {
		return (lootTable, registries) -> {
			String poolName = index == 0 && lootTable.pools.size() == 1 ? MAIN_LOOT_POOL_NAME : LOOT_POOL_NAME_PREFIX + index;
			LootPool pool = lootTable.getPool(poolName);
			if (pool == null) return;

			pool.entries = ImmutableList.<LootPoolEntryContainer>builder()
					.addAll(pool.entries)
					.add(entriesFunction.apply(registries))
					.build();
		};
	}

	private static AnyOfCondition.Builder shouldSmeltLoot(HolderLookup.Provider registries) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
		return AnyOfCondition.anyOf(
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))
				),
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.DIRECT_ATTACKER,
						EntityPredicate.Builder.entity()
								.equipment(
										EntityEquipmentPredicate.Builder.equipment()
												.mainhand(
														ItemPredicate.Builder.item()
																.withSubPredicate(
																		ItemSubPredicates.ENCHANTMENTS,
																		ItemEnchantmentsPredicate.enchantments(
																				List.of(new EnchantmentPredicate(registrylookup.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY))
																		)
																)
												)
								)
				)
		);
	}

	/**
	 * 对指定战利品表应用预注册的修改喵~
	 *
	 * @param key 战利品表资源键喵~
	 * @param lootTable 待修改的战利品表实例喵~
	 */
	public static void applyModification(ResourceKey<LootTable> key, LootTable lootTable, HolderLookup.Provider registries) {
		if (modifications.containsKey(key)) {
			modifications.remove(key).accept(lootTable, registries);
		}

		if (!hasModifications()) {
			modifications = null;
		}
	}

	/**
	 * 判断是否仍存在未应用的战利品表修改喵~
	 *
	 * @return 若仍有待应用修改则返回 true 喵~
	 */
	public static boolean hasModifications() {
		return modifications != null && !modifications.isEmpty();
	}

	private static void registerModification(ResourceKey<LootTable> key, BiConsumer<LootTable, HolderLookup.Provider> modification) {
		modifications.put(key, modification);
	}

	private LootTableModifications() {
	}
}
