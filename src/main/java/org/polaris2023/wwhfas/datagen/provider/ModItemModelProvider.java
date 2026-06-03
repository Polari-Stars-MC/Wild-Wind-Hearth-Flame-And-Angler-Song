package org.polaris2023.wwhfas.datagen.provider;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.block.ModTerrainBlocks;
import org.polaris2023.wwhfas.registry.ModBlocks;
import org.polaris2023.wwhfas.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * 生成模组物品模型数据喵~
 */
public class ModItemModelProvider extends ItemModelProvider {
	/**
	 * 创建物品模型提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param existingFileHelper 已有文件辅助器喵~
	 */
	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, WildWindMod.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
			registerWoodSet(woodSet);
		}
		registerTerrainItems();

		withExistingParent("crab_spawn_egg",mcLoc("item/template_spawn_egg"));
		withExistingParent("crab_bucket", mcLoc("item/generated"))
				.texture("layer0", modLoc("item/crab_bucket"));
		basicItem(ModItems.CRAB_CLAW.get());
		basicItem(ModItems.REEDS.get());
		basicItem(ModItems.CATTAILS.get());
		withExistingParent("piranha_spawn_egg",mcLoc("item/template_spawn_egg"));
		withExistingParent("spiderling_spawn_egg",mcLoc("item/template_spawn_egg"));
		basicItem(ModItems.PIRANHA.get());
		basicItem(ModItems.COOKED_PIRANHA.get());
		basicItem(ModItems.FANG.get());
		withExistingParent("piranha_bucket", mcLoc("item/generated"))
				.texture("layer0", modLoc("item/piranha_bucket"));
	}

	private void registerWoodSet(ModBlocks.WoodSet woodSet) {
		String name = woodSet.name();

		withExistingParent(name + "_fence", mcLoc("block/fence_inventory"))
			.texture("texture", modLoc("block/" + name + "_planks"));
		withExistingParent(name + "_button", mcLoc("block/button_inventory"))
			.texture("texture", modLoc("block/" + name + "_planks"));
		withExistingParent(name + "_door", mcLoc("item/generated"))
			.texture("layer0", modLoc("item/" + name + "_door"));
		if (woodSet.hasTreeBlocks()) {
			withExistingParent(name + "_sapling", mcLoc("item/generated"))
				.texture("layer0", modLoc("block/" + name + "_sapling"));
		}
		withExistingParent(name + "_sign", mcLoc("item/generated"))
			.texture("layer0", modLoc("item/" + name + "_sign"));
		withExistingParent(name + "_hanging_sign", mcLoc("item/generated"))
			.texture("layer0", modLoc("item/" + name + "_hanging_sign"));
		withExistingParent(name + "_boat", mcLoc("item/generated"))
			.texture("layer0", modLoc("item/" + name + "_boat"));
		withExistingParent(name + "_chest_boat", mcLoc("item/generated"))
			.texture("layer0", modLoc("item/" + name + "_chest_boat"));
	}

	private void registerTerrainItems() {
		withExistingParent(ModTerrainBlocks.SCORCHED_GRASS.getId().getPath(), mcLoc("item/generated"))
			.texture("layer0", modLoc("block/scorched_grass"));
		withExistingParent(ModTerrainBlocks.SCORCHED_TWIG.getId().getPath(), mcLoc("item/generated"))
			.texture("layer0", modLoc("block/scorched_twig"));
	}
}
