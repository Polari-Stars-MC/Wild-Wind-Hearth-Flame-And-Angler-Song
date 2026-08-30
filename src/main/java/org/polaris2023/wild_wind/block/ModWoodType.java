package org.polaris2023.wild_wind.block;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.polaris2023.wild_wind.WildWindMod;

/**
 * 模组自定义的木材类型常量喵~
 */
public final class ModWoodType {
	/**
	 * 焦烬木材类型喵~
	 */
	public static final WoodType CINDER = WoodType.register(new WoodType(namespaced("cinder"), ModBlockSetType.CINDER));
	/**
	 * 余烬木材类型喵~
	 */
	public static final WoodType EMBER = WoodType.register(new WoodType(namespaced("ember"), ModBlockSetType.EMBER));
	/**
	 * 杜鹃木材类型喵~
	 */
	public static final WoodType AZALEA = WoodType.register(new WoodType(namespaced("azalea"), ModBlockSetType.AZALEA));

	private ModWoodType() {
	}

	private static String namespaced(String path) {
		return WildWindMod.MOD_ID + ":" + path;
	}
}
