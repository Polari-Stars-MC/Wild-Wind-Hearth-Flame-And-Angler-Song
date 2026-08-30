package org.polaris2023.wild_wind.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.entity.animal.MudcrabVariant;

/**
 * 注册泥沼蟹变种喵~
 */
public class ModMudcrabVariants {
	/**
	 * 泥沼蟹变种延迟注册器喵~
	 */
	public static final DeferredRegister<MudcrabVariant> MUDCRAB_VARIANTS = DeferredRegister.create(ModRegistries.MUDCRAB_VARIANTS, WildWindMod.MOD_ID);

	/**
	 * 温带泥沼蟹变种喵~
	 */
	public static final DeferredHolder<MudcrabVariant, MudcrabVariant> TEMPERATE = register("temperate");
	/**
	 * 寒带泥沼蟹变种喵~
	 */
	public static final DeferredHolder<MudcrabVariant, MudcrabVariant> COLD = register("cold");
	/**
	 * 暖带泥沼蟹变种喵~
	 */
	public static final DeferredHolder<MudcrabVariant, MudcrabVariant> WARM = register("warm");

	/**
	 * 向模组事件总线注册泥沼蟹变种喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		MUDCRAB_VARIANTS.register(modBus);
	}

	private static DeferredHolder<MudcrabVariant, MudcrabVariant> register(String path) {
		return MUDCRAB_VARIANTS.register(path, () -> new MudcrabVariant(WildWindMod.id("textures/entity/mudcrab/" + path + ".png")));
	}

	private ModMudcrabVariants() {
	}
}
