package git.wildwind.wwhfas.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.List;

// TODO: 修复同步问题
public class OmniClawTools implements TooltipComponent {
    public static final OmniClawTools EMPTY = new OmniClawTools(List.of(), 0);
    public static final Codec<OmniClawTools> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.OPTIONAL_CODEC.listOf(0, 4).fieldOf("tools").forGetter(OmniClawTools::getTools),
            Codec.intRange(0, 4).fieldOf("last_selected").forGetter(OmniClawTools::getLastSelected)
    ).apply(i, OmniClawTools::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OmniClawTools> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), OmniClawTools::getTools,
            ByteBufCodecs.VAR_INT, OmniClawTools::getLastSelected,
            OmniClawTools::new
    );

    private final List<ItemStack> toolList;
    private final int lastSelected;

    public OmniClawTools(List<ItemStack> toolStacks, int lastSelected) {
        this.toolList = List.of(
                getStackOrEmpty(toolStacks, 0),
                getStackOrEmpty(toolStacks, 1),
                getStackOrEmpty(toolStacks, 2),
                getStackOrEmpty(toolStacks, 3)
        );
        this.lastSelected = lastSelected;
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

    public List<ItemStack> getTools() {
        return this.toolList;
    }

    public int getLastSelected() {
        return this.lastSelected;
    }

    public int indexOf(ItemStack stack) {
        return this.toolList.indexOf(stack);
    }

    public ItemStack getToolFor(BlockState state) {
        ItemStack tool = ItemStack.EMPTY;

        for (ItemStack stack : getTools()) {
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
                && this.lastSelected == other.lastSelected;
    }

    @Override
    public int hashCode() {
        return 31 * ItemStack.hashStackList(this.toolList) + this.lastSelected;
    }

    public OmniClawTools withLastSelected(int lastSelected) {
        return new OmniClawTools(this.toolList, lastSelected);
    }

    public Mutable toMutable() {
        return new Mutable(this.toolList, this.lastSelected);
    }

    public static class Mutable {
        private final ItemStack[] stacks;
        private int lastSelected;

        private Mutable(Collection<ItemStack> stacks, int lastSelected) {
            this.stacks = stacks.toArray(new ItemStack[4]);
            this.lastSelected = lastSelected;
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
            return swap(ItemStack.EMPTY, this.lastSelected);
        }

        private ItemStack swap(ItemStack stack, int index) {
            ItemStack result = this.stacks[index];
            this.stacks[index] = stack;
            if (!this.stacks[index].isEmpty()) {
                this.lastSelected = index;
            } else {
                for (int i = 0; i < this.stacks.length; i++) {
                    index = (index + 1) % this.stacks.length;
                    if (!this.stacks[index].isEmpty()) {
                        this.lastSelected = index;
                        break;
                    }
                }
            }

            return result;
        }

        public OmniClawTools toImmutable() {
            return new OmniClawTools(List.of(this.stacks), this.lastSelected);
        }
    }
}
