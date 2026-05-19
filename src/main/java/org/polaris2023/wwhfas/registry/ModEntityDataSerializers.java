package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.entity.animal.CrabVariant;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * 注册模组实体数据序列化器喵~
 */
public class ModEntityDataSerializers {
	/**
	 * 实体数据序列化器延迟注册器喵~
	 */
	public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, WildWindMod.MOD_ID);

	/**
	 * 螃蟹变种实体数据序列化器喵~
	 */
	public static final Supplier<EntityDataSerializer<Holder<CrabVariant>>> CRAB_VARIANT =
			ENTITY_DATA_SERIALIZERS.register("crab_variant", () -> EntityDataSerializer.forValueType(CrabVariant.STREAM_CODEC));

	/**
	 * 向模组事件总线注册实体数据序列化器喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		ENTITY_DATA_SERIALIZERS.register(modBus);
	}

	private ModEntityDataSerializers() {
	}
}
