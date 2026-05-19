package git.wildwind.wwhfas.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 模组客户端配置定义喵~
 */
public final class ClientConfig {
	/**
	 * 客户端配置实例喵~
	 */
	public static final ClientConfig INSTANCE;
	/**
	 * 客户端配置规范喵~
	 */
	public static final ModConfigSpec SPEC;

	static {
		Pair<ClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
				.configure(ClientConfig::new);
		INSTANCE = pair.getLeft();
		SPEC = pair.getRight();
	}

	private ClientConfig(ModConfigSpec.Builder builder) {
	}
}
