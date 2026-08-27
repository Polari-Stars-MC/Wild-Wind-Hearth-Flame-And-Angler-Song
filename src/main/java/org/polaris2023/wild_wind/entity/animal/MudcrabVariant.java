package org.polaris2023.wild_wind.entity.animal;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.polaris2023.wild_wind.registry.ModRegistries;

/**
 * 泥沼蟹变种数据喵~
 *
 * @param textureLocation 变种对应的纹理路径喵~
 */
public record MudcrabVariant(ResourceLocation textureLocation) {
	/**
	 * 泥沼蟹变种的编解码器喵~
	 */
	public static final Codec<MudcrabVariant> CODEC = ModRegistries.MUDCRAB_VARIANTS.byNameCodec();
	/**
	 * 泥沼蟹变种持有者的网络编解码器喵~
	 */
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MudcrabVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModRegistries.Keys.MUDCRAB_VARIANT);
}
