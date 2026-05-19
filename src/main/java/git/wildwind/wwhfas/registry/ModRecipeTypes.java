package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.recipe.ArrowFletchingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组配方类型喵~
 */
public final class ModRecipeTypes {
	/**
	 * 配方类型延迟注册器喵~
	 */
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
		DeferredRegister.create(Registries.RECIPE_TYPE, WildWindMod.MOD_ID);

	/**
	 * 制箭台配方类型喵~
	 */
	public static final DeferredHolder<RecipeType<?>, RecipeType<ArrowFletchingRecipe>> ARROW_FLETCHING =
		RECIPE_TYPES.register("arrow_fletching", () -> new RecipeType<>() {
			/**
			 * 获取制箭台配方类型的文本标识喵~
			 *
			 * @return 制箭台配方类型的资源路径字符串喵~
			 */
			@Override
			public String toString() {
				return WildWindMod.MOD_ID + ":arrow_fletching";
			}
		});

	private ModRecipeTypes() {
	}

	/**
	 * 向模组事件总线注册配方类型喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		RECIPE_TYPES.register(modBus);
	}
}
