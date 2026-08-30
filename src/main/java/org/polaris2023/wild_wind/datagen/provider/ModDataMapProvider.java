package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.polaris2023.wild_wind.registry.ModItems;

import java.util.concurrent.CompletableFuture;

/**
 * 生成模组数据映射内容喵~
 */
public class ModDataMapProvider extends DataMapProvider {
	/**
	 * 创建数据映射提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param lookupProvider 注册表查询提供器喵~
	 */
	public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		DataMapProvider.Builder<Compostable, Item> compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
		for (ModItems.WoodItems woodItems : ModItems.WOOD_ITEMS) {
			if (woodItems.hasTreeItems()) {
				compostables.add(woodItems.leaves().get().builtInRegistryHolder(), new Compostable(0.3f, false), false);
				compostables.add(woodItems.sapling().get().builtInRegistryHolder(), new Compostable(0.3f, false), false);
			}
		}
	}
}
