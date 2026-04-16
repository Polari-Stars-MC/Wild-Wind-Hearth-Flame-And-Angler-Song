package git.wildwind.wwhfas.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import git.wildwind.wwhfas.registry.ModDataComponents;
import git.wildwind.wwhfas.registry.ModRecipeSerializers;
import git.wildwind.wwhfas.registry.ModRecipeTypes;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

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

    @Override
    public boolean matches(ArrowFletchingRecipeInput input, Level level) {
        return this.tail.matches(input.tail())
            && this.shaft.matches(input.shaft())
            && this.head.matches(input.head())
            && this.binding.matches(input.binding());
    }

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

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.tail.ingredient(), this.shaft.ingredient(), this.head.ingredient(), this.binding.ingredient());
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.FLETCHING_TABLE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ARROW_FLETCHING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.ARROW_FLETCHING.get();
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.tail, this.shaft, this.head, this.binding).anyMatch(SizedIngredient::isIncomplete);
    }

    public boolean accepts(ArrowFletchingSlot slot, ItemStack stack) {
        return switch (slot) {
            case TAIL -> this.tail.accepts(stack);
            case SHAFT -> this.shaft.accepts(stack);
            case HEAD -> this.head.accepts(stack);
            case BINDING -> this.binding.accepts(stack);
        };
    }

    public SizedIngredient ingredient(ArrowFletchingSlot slot) {
        return switch (slot) {
            case TAIL -> this.tail;
            case SHAFT -> this.shaft;
            case HEAD -> this.head;
            case BINDING -> this.binding;
        };
    }

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
