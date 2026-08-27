package org.polaris2023.wild_wind.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.wild_wind.registry.ModDataComponents;
import org.polaris2023.wild_wind.registry.ModRecipeSerializers;
import org.polaris2023.wild_wind.registry.ModRecipeTypes;

import java.util.stream.Stream;

/**
 * 制箭台配方定义喵~
 */
public final class ArrowFletchingRecipe implements Recipe<ArrowFletchingRecipeInput> {
	private final String group;
	private final CraftingBookCategory category;
	private final SizedIngredient tail;
	private final SizedIngredient shaft;
	private final SizedIngredient head;
	private final SizedIngredient binding;
	private final ResourceLocation tailMaterial;
	private final ResourceLocation shaftMaterial;
	private final ResourceLocation headMaterial;
	private final ItemStack result;

	/**
	 * 创建制箭台配方喵~
	 *
	 * @param group 配方分组喵~
	 * @param category 配方书分类喵~
	 * @param tail 箭尾材料喵~
	 * @param shaft 箭杆材料喵~
	 * @param head 箭头材料喵~
	 * @param binding 绑定材料喵~
	 * @param tailMaterial 结果中记录的箭尾材质喵~
	 * @param shaftMaterial 结果中记录的箭杆材质喵~
	 * @param headMaterial 结果中记录的箭头材质喵~
	 * @param result 合成结果喵~
	 */
	public ArrowFletchingRecipe(
		String group,
		CraftingBookCategory category,
		SizedIngredient tail,
		SizedIngredient shaft,
		SizedIngredient head,
		SizedIngredient binding,
		ResourceLocation tailMaterial,
		ResourceLocation shaftMaterial,
		ResourceLocation headMaterial,
		ItemStack result
	) {
		this.group = group;
		this.category = category;
		this.tail = tail;
		this.shaft = shaft;
		this.head = head;
		this.binding = binding;
		this.tailMaterial = tailMaterial;
		this.shaftMaterial = shaftMaterial;
		this.headMaterial = headMaterial;
		this.result = result;
	}

	/**
	 * 获取配方书分类喵~
	 *
	 * @return 配方书分类喵~
	 */
	public CraftingBookCategory category() {
		return this.category;
	}

	/**
	 * 判断指定槽位是否接受该物品喵~
	 *
	 * @param slot 配方槽位喵~
	 * @param stack 待判断的物品栈喵~
	 * @return 可接受时返回 true 喵~
	 */
	public boolean accepts(ArrowFletchingSlot slot, ItemStack stack) {
		return switch (slot) {
			case TAIL -> this.tail.accepts(stack);
			case SHAFT -> this.shaft.accepts(stack);
			case HEAD -> this.head.accepts(stack);
			case BINDING -> this.binding.accepts(stack);
		};
	}

	/**
	 * 获取指定槽位对应的材料定义喵~
	 *
	 * @param slot 配方槽位喵~
	 * @return 对应的带数量材料喵~
	 */
	public SizedIngredient ingredient(ArrowFletchingSlot slot) {
		return switch (slot) {
			case TAIL -> this.tail;
			case SHAFT -> this.shaft;
			case HEAD -> this.head;
			case BINDING -> this.binding;
		};
	}

	/**
	 * 判断当前输入是否匹配该制箭配方喵~
	 *
	 * @param input 配方输入喵~
	 * @param level 当前世界喵~
	 * @return 匹配时返回 true 喵~
	 */
	@Override
	public boolean matches(ArrowFletchingRecipeInput input, Level level) {
		return this.tail.matches(input.tail())
			&& this.shaft.matches(input.shaft())
			&& this.head.matches(input.head())
			&& this.binding.matches(input.binding());
	}

	/**
	 * 根据输入组装制箭结果喵~
	 *
	 * @param input 配方输入喵~
	 * @param registries 注册表访问器喵~
	 * @return 组装后的结果物品喵~
	 */
	@Override
	public ItemStack assemble(ArrowFletchingRecipeInput input, HolderLookup.Provider registries) {
		ItemStack crafted = this.result.copy();
		if (crafted.is(Items.ARROW)) {
			crafted.set(ModDataComponents.ARROW_TAIL.get(), this.tailMaterial);
			crafted.set(ModDataComponents.ARROW_SHAFT.get(), this.shaftMaterial);
			crafted.set(ModDataComponents.ARROW_HEAD.get(), this.headMaterial);
		}
		return crafted;
	}

	/**
	 * 判断该配方是否可在指定尺寸中制作喵~
	 *
	 * @param width 宽度喵~
	 * @param height 高度喵~
	 * @return 可制作时返回 true 喵~
	 */
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 4;
	}

	/**
	 * 获取该配方在配方书中展示的结果物品喵~
	 *
	 * @param registries 注册表访问器喵~
	 * @return 结果物品喵~
	 */
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return this.result;
	}

	/**
	 * 获取配方展示用的材料列表喵~
	 *
	 * @return 材料列表喵~
	 */
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, this.tail.ingredient(), this.shaft.ingredient(), this.head.ingredient(), this.binding.ingredient());
	}

	/**
	 * 获取配方分组名喵~
	 *
	 * @return 配方分组名喵~
	 */
	@Override
	public String getGroup() {
		return this.group;
	}

	/**
	 * 获取配方提示框显示的图标喵~
	 *
	 * @return 制箭台图标物品喵~
	 */
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.FLETCHING_TABLE);
	}

	/**
	 * 获取该配方的序列化器喵~
	 *
	 * @return 配方序列化器喵~
	 */
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.ARROW_FLETCHING.get();
	}

	/**
	 * 获取该配方的类型喵~
	 *
	 * @return 配方类型喵~
	 */
	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.ARROW_FLETCHING.get();
	}

	/**
	 * 判断该配方定义是否不完整喵~
	 *
	 * @return 任一材料不完整时返回 true 喵~
	 */
	@Override
	public boolean isIncomplete() {
		return Stream.of(this.tail, this.shaft, this.head, this.binding).anyMatch(SizedIngredient::isIncomplete);
	}

	/**
	 * 制箭台配方的序列化器喵~
	 */
	public static final class Serializer implements RecipeSerializer<ArrowFletchingRecipe> {
		private static final MapCodec<ArrowFletchingRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(ArrowFletchingRecipe::getGroup),
				CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ArrowFletchingRecipe::category),
				SizedIngredient.CODEC.fieldOf("tail").forGetter(recipe -> recipe.tail),
				SizedIngredient.CODEC.fieldOf("shaft").forGetter(recipe -> recipe.shaft),
				SizedIngredient.CODEC.fieldOf("head").forGetter(recipe -> recipe.head),
				SizedIngredient.CODEC.fieldOf("binding").forGetter(recipe -> recipe.binding),
				ResourceLocation.CODEC.fieldOf("tail_material").forGetter(recipe -> recipe.tailMaterial),
				ResourceLocation.CODEC.fieldOf("shaft_material").forGetter(recipe -> recipe.shaftMaterial),
				ResourceLocation.CODEC.fieldOf("head_material").forGetter(recipe -> recipe.headMaterial),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
			).apply(instance, ArrowFletchingRecipe::new)
		);
		/**
		 * 制箭配方的网络编解码器喵~
		 */
		public static final StreamCodec<RegistryFriendlyByteBuf, ArrowFletchingRecipe> STREAM_CODEC = StreamCodec.of(
			Serializer::toNetwork,
			Serializer::fromNetwork
		);

		@Override
		public MapCodec<ArrowFletchingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ArrowFletchingRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ArrowFletchingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			return new ArrowFletchingRecipe(
				buffer.readUtf(),
				buffer.readEnum(CraftingBookCategory.class),
				SizedIngredient.STREAM_CODEC.decode(buffer),
				SizedIngredient.STREAM_CODEC.decode(buffer),
				SizedIngredient.STREAM_CODEC.decode(buffer),
				SizedIngredient.STREAM_CODEC.decode(buffer),
				ResourceLocation.STREAM_CODEC.decode(buffer),
				ResourceLocation.STREAM_CODEC.decode(buffer),
				ResourceLocation.STREAM_CODEC.decode(buffer),
				ItemStack.STREAM_CODEC.decode(buffer)
			);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, ArrowFletchingRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeEnum(recipe.category);
			SizedIngredient.STREAM_CODEC.encode(buffer, recipe.tail);
			SizedIngredient.STREAM_CODEC.encode(buffer, recipe.shaft);
			SizedIngredient.STREAM_CODEC.encode(buffer, recipe.head);
			SizedIngredient.STREAM_CODEC.encode(buffer, recipe.binding);
			ResourceLocation.STREAM_CODEC.encode(buffer, recipe.tailMaterial);
			ResourceLocation.STREAM_CODEC.encode(buffer, recipe.shaftMaterial);
			ResourceLocation.STREAM_CODEC.encode(buffer, recipe.headMaterial);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		}
	}
}
