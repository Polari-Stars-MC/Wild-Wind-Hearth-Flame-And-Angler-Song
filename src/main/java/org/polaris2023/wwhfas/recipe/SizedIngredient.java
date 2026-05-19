package org.polaris2023.wwhfas.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 带数量要求的配方材料喵~
 *
 * @param ingredient 材料匹配规则喵~
 * @param count 所需数量喵~
 */
public record SizedIngredient(Ingredient ingredient, int count) {
	/**
	 * 带数量材料的数据编解码器喵~
	 */
	public static final Codec<SizedIngredient> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
			ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count)
		).apply(instance, SizedIngredient::new)
	);
	/**
	 * 带数量材料的网络编解码器喵~
	 */
	public static final StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite(
		Ingredient.CONTENTS_STREAM_CODEC,
		SizedIngredient::ingredient,
		ByteBufCodecs.VAR_INT,
		SizedIngredient::count,
		SizedIngredient::new
	);

	/**
	 * 判断物品栈是否满足材料和数量要求喵~
	 *
	 * @param stack 待检查的物品栈喵~
	 * @return 满足要求时返回 true 喵~
	 */
	public boolean matches(ItemStack stack) {
		return !stack.isEmpty() && stack.getCount() >= this.count && this.ingredient.test(stack);
	}

	/**
	 * 判断物品栈类型是否可接受喵~
	 *
	 * @param stack 待检查的物品栈喵~
	 * @return 可作为该材料时返回 true 喵~
	 */
	public boolean accepts(ItemStack stack) {
		return !stack.isEmpty() && this.ingredient.test(stack);
	}

	/**
	 * 判断该材料定义是否不完整喵~
	 *
	 * @return 若没有任何可匹配物品则返回 true 喵~
	 */
	public boolean isIncomplete() {
		return this.ingredient.hasNoItems();
	}
}
