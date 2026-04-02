package git.wildwind.wwhfas;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(WildWindMod.MOD_ID)
public class WildWindMod {
    public static final String MOD_ID = "wwhfas";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WildWindMod() {
        LOGGER.info("Loaded {}", MOD_ID);
    }
}
