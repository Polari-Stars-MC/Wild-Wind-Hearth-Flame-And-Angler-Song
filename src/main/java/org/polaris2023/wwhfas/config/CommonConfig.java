package org.polaris2023.wwhfas.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 模组通用配置定义喵~
 */
public final class CommonConfig {
	/**
	 * 通用配置实例喵~
	 */
	public static final CommonConfig INSTANCE;
	/**
	 * 通用配置规范喵~
	 */
	public static final ModConfigSpec SPEC;

	static {
		Pair<CommonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
				.configure(CommonConfig::new);
		INSTANCE = pair.getLeft();
		SPEC = pair.getRight();
	}

	private CommonConfig(ModConfigSpec.Builder builder) {
	}
}
