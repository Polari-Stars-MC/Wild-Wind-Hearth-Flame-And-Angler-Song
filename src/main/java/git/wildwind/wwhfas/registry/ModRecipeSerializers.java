package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.recipe.ArrowFletchingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, WildWindMod.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArrowFletchingRecipe>> ARROW_FLETCHING =
            RECIPE_SERIALIZERS.register("arrow_fletching", ArrowFletchingRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus modBus) {
        RECIPE_SERIALIZERS.register(modBus);
    }
}
