package git.wildwind.wwhfas;

import com.mojang.logging.LogUtils;
import git.wildwind.wwhfas.config.ModConfigs;
import git.wildwind.wwhfas.datagen.ModDataGen;
import git.wildwind.wwhfas.registry.ModRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(WildWindMod.MOD_ID)
public class WildWindMod {
    public static final String MOD_ID = "wwhfas";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WildWindMod(IEventBus modBus, ModContainer modContainer) {
        ModRegistries.register(modBus);
        ModDataGen.register(modBus);
        ModConfigs.register(modContainer);
    }
}
