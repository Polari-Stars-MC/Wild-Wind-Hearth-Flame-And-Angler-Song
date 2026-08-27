package org.polaris2023.wild_wind.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.menu.ArrowFletchingMenu;

/**
 * 注册模组菜单类型喵~
 */
public final class ModMenuTypes {
	/**
	 * 菜单类型延迟注册器喵~
	 */
	public static final DeferredRegister<MenuType<?>> MENU_TYPES =
			DeferredRegister.create(Registries.MENU, WildWindMod.MOD_ID);
	/**
	 * 制箭台菜单类型喵~
	 */
	public static final DeferredHolder<MenuType<?>, MenuType<ArrowFletchingMenu>> ARROW_FLETCHING =
			MENU_TYPES.register("arrow_fletching", () -> IMenuTypeExtension.create(ArrowFletchingMenu::new));

	private ModMenuTypes() {
	}

	/**
	 * 向模组事件总线注册菜单类型喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		MENU_TYPES.register(modBus);
	}
}
