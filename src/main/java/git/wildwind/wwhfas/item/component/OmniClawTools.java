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

/**
 * 万用蟹钳内部存储的工具集合喵~
 */
public class OmniClawTools implements TooltipComponent {
    /**
     * 空工具集合喵~
     */
    public static final OmniClawTools EMPTY = new OmniClawTools(List.of(), 0);
    /**
     * 工具集合的数据编解码器喵~
     */
    public static final Codec<OmniClawTools> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.OPTIONAL_CODEC.listOf(0, 4).fieldOf("tools").forGetter(OmniClawTools::getTools),
            Codec.intRange(0, 4).fieldOf("selectedTool").forGetter(OmniClawTools::getSelectedToolIndex)
    ).apply(i, OmniClawTools::new));
    /**
     * 工具集合的网络编解码器喵~
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, OmniClawTools> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), OmniClawTools::getTools,
            ByteBufCodecs.VAR_INT, OmniClawTools::getSelectedToolIndex,
            OmniClawTools::new
    );

    private final List<ItemStack> toolList;
    private final int selectedTool;

    /**
     * 创建一个万用蟹钳工具集合喵~
     *
     * @param toolStacks 工具列表喵~
     * @param selectedTool 当前选中的工具下标喵~
     */
    public OmniClawTools(List<ItemStack> toolStacks, int selectedTool) {
        this.toolList = List.of(
                getStackOrEmpty(toolStacks, 0),
                getStackOrEmpty(toolStacks, 1),
                getStackOrEmpty(toolStacks, 2),
                getStackOrEmpty(toolStacks, 3)
        );
        this.selectedTool = selectedTool;
    }

    /**
     * 判断当前是否没有存储任何工具喵~
     *
     * @return 若没有工具则返回 true 喵~
     */
    public boolean isEmpty() {
        for (ItemStack tool : this.getTools()) {
            if (!tool.isEmpty()) return false;
        }

        return true;
    }

    /**
     * 统计非空工具数量喵~
     *
     * @return 非空工具数量喵~
     */
    public int getNonEmptyItemCount() {
        int count = 0;
        for (ItemStack tool : this.getTools()) {
            if (!tool.isEmpty()) count++;
        }

        return count;
    }

    /**
     * 获取内部工具列表喵~
     *
     * @return 工具列表喵~
     */
    public List<ItemStack> getTools() {
        return this.toolList;
    }

    /**
     * 获取当前选中的工具下标喵~
     *
     * @return 选中工具下标喵~
     */
    public int getSelectedToolIndex() {
        return this.selectedTool;
    }

    /**
     * 获取当前选中的工具喵~
     *
     * @return 当前选中的工具喵~
     */
    public ItemStack getSelectedTool() {
        return this.toolList.get(this.selectedTool);
    }

    /**
     * 查找指定工具在列表中的下标喵~
     *
     * @param stack 待查找的工具喵~
     * @return 工具下标，不存在时返回 -1 喵~
     */
    public int indexOf(ItemStack stack) {
        if (stack.isEmpty()) return -1;

        return this.toolList.indexOf(stack);
    }

    /**
     * 按物品能力查找可用工具喵~
     *
     * @param ability 目标能力喵~
     * @return 匹配到的工具，若不存在则返回空物品喵~
     */
    public ItemStack getToolByAbility(ItemAbility ability) {
        ItemStack tool = ItemStack.EMPTY;

        for (ItemStack stack : this.getTools()) {
            if (stack.isEmpty()) continue;
            if (stack.canPerformAction(ability)) return stack;
        }

        return tool;
    }

    /**
     * 为指定方块状态选择最合适的工具喵~
     *
     * @param state 方块状态喵~
     * @return 匹配到的工具，若不存在则返回空物品喵~
     */
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

    /**
     * 返回一个修改选中下标后的新实例喵~
     *
     * @param index 新的选中下标喵~
     * @return 新的不可变工具集合喵~
     */
    public OmniClawTools withSelectIndex(int index) {
        return new OmniClawTools(this.toolList, index);
    }

    /**
     * 转换为可变操作器喵~
     *
     * @return 可变工具集合喵~
     */
    public Mutable toMutable() {
        return new Mutable(this.toolList, this.selectedTool);
    }

    /**
     * 万用蟹钳工具集合的可变操作器喵~
     */
    public static class Mutable {
        private final ItemStack[] stacks;
        private int selectedTool;

        private Mutable(Collection<ItemStack> stacks, int selectedTool) {
            this.stacks = stacks.toArray(new ItemStack[4]);
            this.selectedTool = selectedTool;
        }

        /**
         * 获取槽位数量喵~
         *
         * @return 槽位数量喵~
         */
        public int size() {
            return this.stacks.length;
        }

        /**
         * 获取当前选中的工具下标喵~
         *
         * @return 选中工具下标喵~
         */
        public int getSelectedToolIndex() {
            return this.selectedTool;
        }

        /**
         * 获取指定下标处的工具喵~
         *
         * @param index 槽位下标喵~
         * @return 对应槽位中的工具喵~
         */
        public ItemStack get(int index) {
            return this.stacks[index];
        }

        /**
         * 设置指定下标处的工具喵~
         *
         * @param stack 要设置的工具喵~
         * @param index 槽位下标喵~
         * @return 当前操作器喵~
         */
        public Mutable set(ItemStack stack, int index) {
            this.stacks[index] = stack;
            return this;
        }

        /**
         * 设置当前选中工具喵~
         *
         * @param index 新的选中下标喵~
         * @return 当前操作器喵~
         */
        public Mutable select(int index) {
            this.selectedTool = index;
            return this;
        }

        /**
         * 滚动选择到下一个非空工具喵~
         *
         * @return 当前操作器喵~
         */
        public Mutable scrollSelectToNoEmptyItem() {
            int index = this.selectedTool + 1;
            for (int i = 0; i < this.stacks.length; i++) {
                index = (index + i) % this.stacks.length;
                ItemStack stack = this.stacks[index];
                if (!stack.isEmpty()) return select(index);
            }

            return select(0);
        }

        /**
         * 将工具插入对应类型槽位喵~
         *
         * @param stack 待插入的工具喵~
         * @return 被替换出的物品喵~
         */
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

        /**
         * 移除当前选中的工具喵~
         *
         * @return 被移除的工具喵~
         */
        public ItemStack removeSelected() {
            return swap(ItemStack.EMPTY, this.selectedTool);
        }

        /**
         * 转换为不可变工具集合喵~
         *
         * @return 不可变工具集合喵~
         */
        public OmniClawTools toImmutable() {
            return new OmniClawTools(List.of(this.stacks), this.selectedTool);
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
    }

    private static ItemStack getStackOrEmpty(List<ItemStack> stacks, int index) {
        return index < 0 || index >= stacks.size()
                ? ItemStack.EMPTY
                : stacks.get(index);
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
}
