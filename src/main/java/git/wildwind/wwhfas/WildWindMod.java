package git.wildwind.wwhfas;

import com.mojang.logging.LogUtils;
import git.wildwind.wwhfas.config.ModConfigs;
import git.wildwind.wwhfas.datagen.ModDataGen;
import git.wildwind.wwhfas.loot.LootTableModifications;
import git.wildwind.wwhfas.registry.ModCommonSetup;
import git.wildwind.wwhfas.registry.ModRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod(WildWindMod.MOD_ID)
public class WildWindMod {
    public static final String MOD_ID = "wwhfas";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WildWindMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::registerRegistries);
        ModRegistries.registerAllEntries(modEventBus);
        ModCommonSetup.register(modEventBus);
        ModDataGen.register(modEventBus);
        ModConfigs.register(modContainer);
    }

    @SubscribeEvent
    void modifyLoots(LootTableLoadEvent event) {
        if (!LootTableModifications.hasModifications()) return;
        LootTableModifications.applyModification(event.getKey(), event.getTable());
    }

    void registerRegistries(NewRegistryEvent event) {
        event.register(ModRegistries.CRAB_VARIANTS);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
