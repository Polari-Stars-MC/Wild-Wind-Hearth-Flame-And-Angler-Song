package git.wildwind.wwhfas.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Collection;
import java.util.List;

public class OmniClawTools implements TooltipComponent {
    public static final OmniClawTools EMPTY = new OmniClawTools(List.of(), 0);
    public static final Codec<OmniClawTools> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.OPTIONAL_CODEC.listOf(0, 4).fieldOf("tools").forGetter(OmniClawTools::getTools),
            Codec.intRange(0, 4).fieldOf("selectedTool").forGetter(OmniClawTools::getSelectedToolIndex)
    ).apply(i, OmniClawTools::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OmniClawTools> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), OmniClawTools::getTools,
            ByteBufCodecs.VAR_INT, OmniClawTools::getSelectedToolIndex,
            OmniClawTools::new
    );

    private final List<ItemStack> toolList;
    private final int selectedTool;

    public OmniClawTools(List<ItemStack> toolStacks, int selectedTool) {
        this.toolList = List.of(
                getStackOrEmpty(toolStacks, 0),
                getStackOrEmpty(toolStacks, 1),
                getStackOrEmpty(toolStacks, 2),
                getStackOrEmpty(toolStacks, 3)
        );
        this.selectedTool = selectedTool;
    }

    private static ItemStack getStackOrEmpty(List<ItemStack> stacks, int index) {
        return index < 0 || index >= stacks.size()
                ? ItemStack.EMPTY
                : stacks.get(index);
    }

    public boolean isEmpty() {
        for (ItemStack tool : this.getTools()) {
            if (!tool.isEmpty()) return false;
        }

        return true;
    }

    public int getNonEmptyItemCount() {
        int count = 0;
        for (ItemStack tool : this.getTools()) {
            if (!tool.isEmpty()) count++;
        }

        return count;
    }

    public List<ItemStack> getTools() {
        return this.toolList;
    }

    public int getSelectedToolIndex() {
        return this.selectedTool;
    }

    public ItemStack getSelectedTool() {
        return this.toolList.get(this.selectedTool);
    }

    public int indexOf(ItemStack stack) {
        if (stack.isEmpty()) return -1;

        return this.toolList.indexOf(stack);
    }

    public ItemStack getToolByAbility(ItemAbility ability) {
        ItemStack tool = ItemStack.EMPTY;

        for (ItemStack stack : this.getTools()) {
            if (stack.isEmpty()) continue;
            if (stack.canPerformAction(ability)) return stack;
        }

        return tool;
    }

    public ItemStack getToolFor(BlockState state) {
        ItemStack tool = ItemStack.EMPTY;

        for (ItemStack stack : this.getTools()) {
            if (!tool.isEmpty()) {
                if (stack.isCorrectToolForDrops(state) && stack.getDestroySpeed(state) > tool.getDestroySpeed(state)) {
                    tool = stack;
                }

                continue;
            }

            if (stack.isCorrectToolForDrops(state)) {
                tool = stack;
            }
        }

        return tool;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        return obj instanceof OmniClawTools other
                && ItemStack.listMatches(this.toolList, other.toolList)
                && this.selectedTool == other.selectedTool;
    }

    @Override
    public int hashCode() {
        return 31 * ItemStack.hashStackList(this.toolList) + this.selectedTool;
    }

    public OmniClawTools withSelectIndex(int index) {
        return new OmniClawTools(this.toolList, index);
    }

    public Mutable toMutable() {
        return new Mutable(this.toolList, this.selectedTool);
    }

    public static class Mutable {
        private final ItemStack[] stacks;
        private int selectedTool;

        private Mutable(Collection<ItemStack> stacks, int selectedTool) {
            this.stacks = stacks.toArray(new ItemStack[4]);
            this.selectedTool = selectedTool;
        }

        public int size() {
            return this.stacks.length;
        }

        public int getSelectedToolIndex() {
            return this.selectedTool;
        }

        public ItemStack get(int index) {
            return this.stacks[index];
        }

        public Mutable set(ItemStack stack, int index) {
            this.stacks[index] = stack;
            return this;
        }

        public Mutable select(int index) {
            this.selectedTool = index;
            return this;
        }

        public Mutable scrollSelectToNoEmptyItem() {
            int index = this.selectedTool + 1;
            for (int i = 0; i < this.stacks.length; i++) {
                index = (index + i) % this.stacks.length;
                ItemStack stack = this.stacks[index];
                if (!stack.isEmpty()) return select(index);
            }

            return select(0);
        }


        public ItemStack insert(ItemStack stack) {
            Item item = stack.getItem();
            ItemStack output = stack;
            switch (item) {
                case PickaxeItem pickaxeItem -> output = swap(stack, 0);
                case AxeItem axeItem -> output = swap(stack, 1);
                case ShovelItem shovelItem -> output = swap(stack, 2);
                case HoeItem hoeItem -> output = swap(stack, 3);
                default -> {
                }
            }

            return output;
        }

        public ItemStack removeSelected() {
            return swap(ItemStack.EMPTY, this.selectedTool);
        }

        private ItemStack swap(ItemStack stack, int index) {
            ItemStack result = this.stacks[index];
            this.stacks[index] = stack;
            if (!this.stacks[index].isEmpty()) {
                this.selectedTool = index;
            } else {
                for (int i = 0; i < this.stacks.length; i++) {
                    index = (index + 1) % this.stacks.length;
                    if (!this.stacks[index].isEmpty()) {
                        this.selectedTool = index;
                        break;
                    }
                }
            }

            return result;
        }

        public OmniClawTools toImmutable() {
            return new OmniClawTools(List.of(this.stacks), this.selectedTool);
        }
    }
}
