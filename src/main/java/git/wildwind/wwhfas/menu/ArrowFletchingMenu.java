package git.wildwind.wwhfas.menu;

import git.wildwind.wwhfas.recipe.ArrowFletchingRecipe;
import git.wildwind.wwhfas.recipe.ArrowFletchingRecipeInput;
import git.wildwind.wwhfas.recipe.ArrowFletchingSlot;
import git.wildwind.wwhfas.registry.ModMenuTypes;
import git.wildwind.wwhfas.registry.ModRecipeTypes;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class ArrowFletchingMenu extends ItemCombinerMenu {
    private static final int TAIL_SLOT = 0;
    private static final int SHAFT_SLOT = 1;
    private static final int HEAD_SLOT = 2;
    private static final int BINDING_SLOT = 3;
    private static final int RESULT_SLOT = 4;

    private final Level level;
    private final DataSlot hasError = DataSlot.standalone();
    @Nullable
    private RecipeHolder<ArrowFletchingRecipe> selectedRecipe;

    public ArrowFletchingMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, ContainerLevelAccess.create(playerInventory.player.level(), extraData.readBlockPos()));
    }

    public ArrowFletchingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.ARROW_FLETCHING.get(), containerId, playerInventory, access);
        this.level = playerInventory.player.level();
        this.addDataSlot(this.hasError);
    }

    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return hasStack;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        if (this.selectedRecipe != null) {
            this.resultSlots.awardUsedRecipes(player, NonNullList.of(
                ItemStack.EMPTY,
                this.inputSlots.getItem(TAIL_SLOT).copy(),
                this.inputSlots.getItem(SHAFT_SLOT).copy(),
                this.inputSlots.getItem(HEAD_SLOT).copy(),
                this.inputSlots.getItem(BINDING_SLOT).copy()
            ));
            this.consumeInput(TAIL_SLOT, this.selectedRecipe.value().ingredient(ArrowFletchingSlot.TAIL).count(), player);
            this.consumeInput(SHAFT_SLOT, this.selectedRecipe.value().ingredient(ArrowFletchingSlot.SHAFT).count(), player);
            this.consumeInput(HEAD_SLOT, this.selectedRecipe.value().ingredient(ArrowFletchingSlot.HEAD).count(), player);
            this.consumeInput(BINDING_SLOT, this.selectedRecipe.value().ingredient(ArrowFletchingSlot.BINDING).count(), player);
        }
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(Blocks.FLETCHING_TABLE);
    }

    @Override
    public void createResult() {
        ArrowFletchingRecipeInput input = this.createRecipeInput();
        Optional<RecipeHolder<ArrowFletchingRecipe>> recipe = this.level.getRecipeManager()
            .getRecipeFor(ModRecipeTypes.ARROW_FLETCHING.get(), input, this.level, this.selectedRecipe);
        if (recipe.isPresent()) {
            this.selectedRecipe = recipe.get();
            this.resultSlots.setRecipeUsed(recipe.get());
            this.resultSlots.setItem(0, recipe.get().value().assemble(input, this.level.registryAccess()));
            this.hasError.set(0);
        } else {
            this.selectedRecipe = null;
            this.resultSlots.setRecipeUsed(null);
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.hasError.set(this.hasAnyInputItem() ? 1 : 0);
        }

        this.broadcastChanges();
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
            .withSlot(TAIL_SLOT, 18, 22, stack -> this.accepts(ArrowFletchingSlot.TAIL, stack))
            .withSlot(SHAFT_SLOT, 43, 22, stack -> this.accepts(ArrowFletchingSlot.SHAFT, stack))
            .withSlot(HEAD_SLOT, 68, 22, stack -> this.accepts(ArrowFletchingSlot.HEAD, stack))
            .withSlot(BINDING_SLOT, 43, 52, stack -> this.accepts(ArrowFletchingSlot.BINDING, stack))
            .withResultSlot(RESULT_SLOT, 125, 32)
            .build();
    }

    @Override
    protected boolean canMoveIntoInputSlots(ItemStack stack) {
        return this.accepts(ArrowFletchingSlot.TAIL, stack)
            || this.accepts(ArrowFletchingSlot.SHAFT, stack)
            || this.accepts(ArrowFletchingSlot.HEAD, stack)
            || this.accepts(ArrowFletchingSlot.BINDING, stack);
    }

    public boolean shouldShowError() {
        return this.hasError.get() == 1;
    }

    public boolean hasAnyInputItem() {
        return !this.inputSlots.getItem(TAIL_SLOT).isEmpty()
            || !this.inputSlots.getItem(SHAFT_SLOT).isEmpty()
            || !this.inputSlots.getItem(HEAD_SLOT).isEmpty()
            || !this.inputSlots.getItem(BINDING_SLOT).isEmpty();
    }

    private boolean accepts(ArrowFletchingSlot slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return this.level.getRecipeManager()
            .getAllRecipesFor(ModRecipeTypes.ARROW_FLETCHING.get())
            .stream()
            .anyMatch(recipe -> recipe.value().accepts(slot, stack));
    }

    private ArrowFletchingRecipeInput createRecipeInput() {
        return new ArrowFletchingRecipeInput(
            this.inputSlots.getItem(TAIL_SLOT),
            this.inputSlots.getItem(SHAFT_SLOT),
            this.inputSlots.getItem(HEAD_SLOT),
            this.inputSlots.getItem(BINDING_SLOT)
        );
    }

    private void consumeInput(int slotIndex, int amount, Player player) {
        ItemStack stack = this.inputSlots.getItem(slotIndex);
        if (stack.isEmpty() || amount <= 0) {
            return;
        }

        ItemStack remainder = stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem().copyWithCount(amount) : ItemStack.EMPTY;
        this.inputSlots.removeItem(slotIndex, amount);

        if (remainder.isEmpty()) {
            return;
        }

        ItemStack current = this.inputSlots.getItem(slotIndex);
        if (current.isEmpty()) {
            this.inputSlots.setItem(slotIndex, remainder);
            return;
        }

        if (ItemStack.isSameItemSameComponents(current, remainder)) {
            current.grow(remainder.getCount());
            this.inputSlots.setItem(slotIndex, current);
            return;
        }

        if (!player.getInventory().add(remainder)) {
            player.drop(remainder, false);
        }
    }
}
