package git.wildwind.wwhfas.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ArrowFletchingRecipeInput(ItemStack tail, ItemStack shaft, ItemStack head, ItemStack binding) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> this.tail;
            case 1 -> this.shaft;
            case 2 -> this.head;
            case 3 -> this.binding;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + index);
        };
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return this.tail.isEmpty() && this.shaft.isEmpty() && this.head.isEmpty() && this.binding.isEmpty();
    }
}
