package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.polaris2023.wild_wind.registry.ModBlocks;
import org.polaris2023.wild_wind.registry.ModItems;

import java.util.concurrent.CompletableFuture;

/**
 * 生成模组合成配方数据喵~
 */
public class ModRecipeProvider extends RecipeProvider {
	/**
	 * 创建配方提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param lookupProvider 注册表查询提供器喵~
	 */
	public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		for (int index = 0; index < ModBlocks.WOOD_SETS.size(); index++) {
			buildWoodRecipes(recipeOutput, ModBlocks.WOOD_SETS.get(index), ModItems.WOOD_ITEMS.get(index));
		}

		buildCraftingTable(recipeOutput);
		buildSimpleCooking(recipeOutput);
	}

	private void buildSimpleCooking(RecipeOutput recipeOutput) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.PIRANHA), RecipeCategory.FOOD, ModItems.COOKED_PIRANHA, 0.35F, 200)
				.unlockedBy("has_piranha", has(ModItems.PIRANHA))
				.save(recipeOutput);
	}

	private void buildCraftingTable(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OMNI_CLAW.get())
				.define('I', Items.COPPER_INGOT)
				.define('C', ModItems.CRAB_CLAW.get())
				.pattern("ICC")
				.pattern("IIC")
				.pattern("III")
				.unlockedBy("has_" + ModItems.CRAB_CLAW.getId().getPath(), has(ModItems.OMNI_CLAW.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER)
				.requires(ModItems.REEDS.get(), 3)
				.unlockedBy("has" + ModItems.REEDS.getId().getPath(), has(ModItems.REEDS.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING)
				.requires(ModItems.CATTAILS.get(), 2)
				.unlockedBy("has" + ModItems.CATTAILS.getId().getPath(), has(ModItems.CATTAILS.get()))
				.save(recipeOutput);
	}

	private void buildWoodRecipes(RecipeOutput recipeOutput, ModBlocks.WoodSet woodSet, ModItems.WoodItems woodItems) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, woodSet.planks().get(), 4)
				.requires(woodSet.log().get())
				.unlockedBy("has_" + woodSet.name() + "_log", has(woodSet.log().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodSet.wood().get(), 3)
				.define('#', woodSet.log().get())
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_" + woodSet.name() + "_log", has(woodSet.log().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodSet.stairs().get(), 4)
				.define('#', woodSet.planks().get())
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodSet.slab().get(), 6)
				.define('#', woodSet.planks().get())
				.pattern("###")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.fence().get(), 3)
				.define('#', woodSet.planks().get())
				.define('S', Items.STICK)
				.pattern("#S#")
				.pattern("#S#")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, woodSet.fenceGate().get())
				.define('#', woodSet.planks().get())
				.define('S', Items.STICK)
				.pattern("S#S")
				.pattern("S#S")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, woodSet.door().get(), 3)
				.define('#', woodSet.planks().get())
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, woodSet.trapdoor().get(), 2)
				.define('#', woodSet.planks().get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, woodSet.pressurePlate().get())
				.define('#', woodSet.planks().get())
				.pattern("##")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, woodSet.button().get())
				.requires(woodSet.planks().get())
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.sign().get(), 3)
				.define('#', woodSet.planks().get())
				.define('S', Items.STICK)
				.pattern("###")
				.pattern("###")
				.pattern(" S ")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.hangingSign().get(), 6)
				.define('#', woodSet.strippedLog().get())
				.define('C', Items.CHAIN)
				.pattern("C C")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_" + woodSet.name() + "_stripped_log", has(woodSet.strippedLog().get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, woodItems.boat().get())
				.define('#', woodSet.planks().get())
				.pattern("# #")
				.pattern("###")
				.unlockedBy("has_" + woodSet.name() + "_planks", has(woodSet.planks().get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, woodItems.chestBoat().get())
				.requires(woodItems.boat().get())
				.requires(Items.CHEST)
				.unlockedBy("has_" + woodSet.name() + "_boat", has(woodItems.boat().get()))
				.save(recipeOutput);
	}
}
