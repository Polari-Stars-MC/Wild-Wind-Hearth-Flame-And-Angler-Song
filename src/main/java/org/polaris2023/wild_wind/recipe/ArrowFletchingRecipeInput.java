package org.polaris2023.wild_wind.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * 箭矢制箭配方的输入数据喵~
 *
 * @param tail 箭尾材料喵~
 * @param shaft 箭杆材料喵~
 * @param head 箭头材料喵~
 * @param binding 绑定材料喵~
 */
public record ArrowFletchingRecipeInput(ItemStack tail, ItemStack shaft, ItemStack head, ItemStack binding) implements RecipeInput {
	/**
	 * 获取指定槽位的输入物品喵~
	 *
	 * @param index 槽位下标喵~
	 * @return 对应槽位中的物品栈喵~
	 */
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

	/**
	 * 获取输入槽位总数喵~
	 *
	 * @return 输入槽位数量喵~
	 */
	@Override
	public int size() {
		return 4;
	}

	/**
	 * 判断当前输入是否全部为空喵~
	 *
	 * @return 全部为空时返回 true 喵~
	 */
	@Override
	public boolean isEmpty() {
		return this.tail.isEmpty() && this.shaft.isEmpty() && this.head.isEmpty() && this.binding.isEmpty();
	}
}
