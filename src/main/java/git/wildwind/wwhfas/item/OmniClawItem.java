package git.wildwind.wwhfas.item;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.item.component.OmniClawTools;
import git.wildwind.wwhfas.registry.ModAttributes;
import git.wildwind.wwhfas.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class OmniClawItem extends Item {
    public static final ResourceLocation BLOCK_INTERACTION_RANGE_ID = WildWindMod.id("omni_claw_block_interaction_range");
    public static final ResourceLocation ENTITY_INTERACTION_RANGE_ID = WildWindMod.id("omni_claw_entity_interaction_range");
    public static final ResourceLocation EXTRA_ITEM_PICKUP_RANGE_ID = WildWindMod.id("omni_claw_extra_item_pickup_range");

    public OmniClawItem(Properties properties) {
        super(properties);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ItemStack tool = getLastSelectedTool(stack);
        if (tool.isEmpty()) {
            super.setDamage(stack, damage);
            return;
        }

        ItemStack copiedTool = tool.copy();
        copiedTool.setDamageValue(damage);
        OmniClawTools tools = stack.get(ModDataComponents.OMNI_CLAW_TOOLS);
        stack.set(ModDataComponents.OMNI_CLAW_TOOLS, tools
                .toMutable()
                .set(copiedTool, tools.indexOf(tool))
                .toImmutable()
        );
    }

    @Override
    public int getDamage(ItemStack omniStack) {
        return toolOr(omniStack, stack -> stack.getItem().getDamage(stack), () -> super.getDamage(omniStack));
    }

    @Override
    public int getMaxDamage(ItemStack omniStack) {
        return toolOr(omniStack, stack -> stack.getItem().getMaxDamage(stack), () -> super.getMaxDamage(omniStack));
    }

    @Override
    public boolean isDamageable(ItemStack omniStack) {
        return toolOr(omniStack, stack -> stack.getItem().isDamageable(stack), () -> super.isDamageable(omniStack));
    }

    public static <T> T toolOr(ItemStack omniStack, Function<ItemStack, T> resultFunction, Supplier<T> or) {
        ItemStack tool = getLastSelectedTool(omniStack);
        return tool.isEmpty() ? or.get() : resultFunction.apply(tool);
    }

    public static ItemStack getLastSelectedTool(ItemStack omniClawStack) {
        if (!omniClawStack.has(ModDataComponents.OMNI_CLAW_TOOLS)) return ItemStack.EMPTY;

        OmniClawTools tools = omniClawStack.get(ModDataComponents.OMNI_CLAW_TOOLS);
        return tools.getTools().get(tools.getLastSelected());
    }

    public static ItemStack getToolFor(ItemStack omniClawStack, BlockState state) {
        if (!omniClawStack.has(ModDataComponents.OMNI_CLAW_TOOLS)) return ItemStack.EMPTY;

        OmniClawTools tools = omniClawStack.get(ModDataComponents.OMNI_CLAW_TOOLS);
        ItemStack result = tools.getToolFor(state);
        int lastSelected = tools.indexOf(result);
        if (lastSelected != -1) omniClawStack.update(ModDataComponents.OMNI_CLAW_TOOLS, tools, operator -> operator.withSelectIndex(lastSelected));

        return result;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide && !getToolFor(stack, state).isEmpty()) {
            stack.hurtAndBreak(1, miningEntity, EquipmentSlot.MAINHAND);
            return true;
        }

        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BLOCK_INTERACTION_RANGE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ENTITY_INTERACTION_RANGE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(ModAttributes.EXTRA_ITEM_PICKUP_RANGE, new AttributeModifier(EXTRA_ITEM_PICKUP_RANGE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack omniStack, BlockState state) {
        return toolOr(omniStack, stack -> stack.isCorrectToolForDrops(state), () -> super.isCorrectToolForDrops(omniStack, state));
    }

    @Override
    public ItemEnchantments getAllEnchantments(ItemStack omniStack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        return toolOr(omniStack, stack -> stack.getAllEnchantments(lookup), () -> super.getAllEnchantments(omniStack, lookup));
    }

    @Override
    public float getDestroySpeed(ItemStack omniStack, BlockState state) {
        return toolOr(omniStack, stack -> stack.getDestroySpeed(state), () -> super.getDestroySpeed(omniStack, state));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(ModDataComponents.OMNI_CLAW_TOOLS))
                : Optional.empty();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (stack.has(ModDataComponents.OMNI_CLAW_TOOLS)) {
                OmniClawTools toolsComponent = stack.get(ModDataComponents.OMNI_CLAW_TOOLS);
                if (toolsComponent.isEmpty()) return super.use(level, player, usedHand);

                stack.update(
                        ModDataComponents.OMNI_CLAW_TOOLS,
                        toolsComponent,
                        tools -> tools.toMutable().scrollSelectToNoEmptyItem().toImmutable()
                );

                return InteractionResultHolder.success(stack);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        }

        return swapItem(stack, slot.getItem(), player, SlotAccess.of(slot::getItem, slot::set));
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access
    ) {
        if (stack.getCount() != 1) return false;
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }

        return swapItem(stack, other, player, access);
    }

    private boolean swapItem(ItemStack omniClaw, ItemStack other, Player player, SlotAccess access) {
        OmniClawTools tools = omniClaw.get(ModDataComponents.OMNI_CLAW_TOOLS);
        if (tools == null) {
            return false;
        }

        OmniClawTools.Mutable mutable = tools.toMutable();
        if (other.isEmpty()) {
            ItemStack output = mutable.removeSelected();
            if (!output.isEmpty()) {
                this.playRemoveOneSound(player);
                access.set(output);
            }
        } else {
            ItemStack output = mutable.insert(other);
            if (!ItemStack.isSameItemSameComponents(other, output)) {
                this.playInsertSound(player);
                access.set(output);
            }
        }

        omniClaw.set(ModDataComponents.OMNI_CLAW_TOOLS, mutable.toImmutable());
        return true;
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
