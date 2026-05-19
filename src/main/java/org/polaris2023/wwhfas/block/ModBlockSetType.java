package org.polaris2023.wwhfas.block;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;

/**
 * 模组自定义的方块集类型常量喵~
 */
public final class ModBlockSetType {
	/**
	 * 焦烬木系方块集类型喵~
	 */
	public static final BlockSetType CINDER = BlockSetType.register(new BlockSetType(namespaced("cinder")));
	/**
	 * 余烬木系方块集类型喵~
	 */
	public static final BlockSetType EMBER = BlockSetType.register(new BlockSetType(namespaced("ember")));
	/**
	 * 杜鹃木系方块集类型喵~
	 */
	public static final BlockSetType AZALEA = BlockSetType.register(new BlockSetType(namespaced("azalea")));

	private ModBlockSetType() {
	}

	private static String namespaced(String path) {
		return WildWindMod.MOD_ID + ":" + path;
	}
}
