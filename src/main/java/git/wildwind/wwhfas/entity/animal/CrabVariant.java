package git.wildwind.wwhfas.entity.animal;

import com.mojang.serialization.Codec;
import git.wildwind.wwhfas.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record CrabVariant(ResourceLocation textureLocation) {
    public static final Codec<CrabVariant> CODEC = ModRegistries.CRAB_VARIANT.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CrabVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModRegistries.Keys.CRAB_VARIANT);
}
