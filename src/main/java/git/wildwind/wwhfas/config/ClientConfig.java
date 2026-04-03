package git.wildwind.wwhfas.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ClientConfig {
    public static final ClientConfig INSTANCE;
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
