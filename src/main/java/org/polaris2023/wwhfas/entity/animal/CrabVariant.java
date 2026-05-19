package org.polaris2023.wwhfas.entity.animal;

import com.mojang.serialization.Codec;
import org.polaris2023.wwhfas.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * 螃蟹变种数据喵~
 *
 * @param textureLocation 变种对应的纹理路径喵~
 */
public record CrabVariant(ResourceLocation textureLocation) {
	/**
	 * 螃蟹变种的编解码器喵~
	 */
	public static final Codec<CrabVariant> CODEC = ModRegistries.CRAB_VARIANTS.byNameCodec();
	/**
	 * 螃蟹变种持有者的网络编解码器喵~
	 */
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CrabVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModRegistries.Keys.CRAB_VARIANT);
}
