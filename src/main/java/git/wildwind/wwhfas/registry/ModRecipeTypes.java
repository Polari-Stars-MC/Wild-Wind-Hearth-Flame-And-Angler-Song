package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.recipe.ArrowFletchingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(Registries.RECIPE_TYPE, WildWindMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ArrowFletchingRecipe>> ARROW_FLETCHING =
        RECIPE_TYPES.register("arrow_fletching", () -> new RecipeType<>() {
            @Override
            public String toString() {
                return WildWindMod.MOD_ID + ":arrow_fletching";
            }
        });

    private ModRecipeTypes() {
    }

    public static void register(IEventBus modBus) {
        RECIPE_TYPES.register(modBus);
    }
}
