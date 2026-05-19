package git.wildwind.wwhfas.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

/**
 * 模组配置注册入口喵~
 */
public final class ModConfigs {
    private ModConfigs() {
    }

    /**
     * 向模组容器注册全部配置规范喵~
     *
     * @param container 模组容器喵~
     */
    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
