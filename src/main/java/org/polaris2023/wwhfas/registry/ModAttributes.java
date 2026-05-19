package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组属性喵~
 */
public class ModAttributes {
	/**
	 * 模组属性延迟注册器喵~
	 */
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, WildWindMod.MOD_ID);

	/**
	 * 额外物品拾取距离属性喵~
	 */
	public static final DeferredHolder<Attribute, Attribute> EXTRA_ITEM_PICKUP_RANGE =
			ATTRIBUTES.register("extra_item_pickup_range", () -> new RangedAttribute(
					"attributes.wwhfas.extra_item_pickup_range",
					0.0,
					0.0,
					10.0
			));

	private ModAttributes() {
	}
}
