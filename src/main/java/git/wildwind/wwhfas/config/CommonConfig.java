package git.wildwind.wwhfas.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class CommonConfig {
    public static final CommonConfig INSTANCE;
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
