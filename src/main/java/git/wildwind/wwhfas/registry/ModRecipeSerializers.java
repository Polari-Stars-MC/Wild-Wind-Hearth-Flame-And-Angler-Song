package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.recipe.ArrowFletchingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组配方序列化器喵~
 */
public final class ModRecipeSerializers {
    /**
     * 配方序列化器延迟注册器喵~
     */
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, WildWindMod.MOD_ID);
    /**
     * 制箭台配方序列化器喵~
     */
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArrowFletchingRecipe>> ARROW_FLETCHING =
            RECIPE_SERIALIZERS.register("arrow_fletching", ArrowFletchingRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }

    /**
     * 向模组事件总线注册配方序列化器喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        RECIPE_SERIALIZERS.register(modBus);
    }
}
