package git.wildwind.wwhfas.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record SizedIngredient(Ingredient ingredient, int count) {
    public static final Codec<SizedIngredient> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count)
        ).apply(instance, SizedIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC,
        SizedIngredient::ingredient,
        ByteBufCodecs.VAR_INT,
        SizedIngredient::count,
        SizedIngredient::new
    );

    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && stack.getCount() >= this.count && this.ingredient.test(stack);
    }

    public boolean accepts(ItemStack stack) {
        return !stack.isEmpty() && this.ingredient.test(stack);
    }

    public boolean isIncomplete() {
        return this.ingredient.hasNoItems();
    }
}
