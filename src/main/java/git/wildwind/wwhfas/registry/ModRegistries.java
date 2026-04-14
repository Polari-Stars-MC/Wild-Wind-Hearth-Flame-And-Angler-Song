package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import net.neoforged.bus.api.IEventBus;

public final class ModRegistries {
    private ModRegistries() {
    }

    public static void register(IEventBus modBus) {
        ModBlocks.register(modBus);
        ModTerrainBlocks.register(modBus);
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
        ModSounds.register(modBus);
        ModMenuTypes.register(modBus);
        ModDataAttachments.register(modBus);
        ModBlockEntities.register(modBus);
        ModEntities.register(modBus);
        ModParticleTypes.register(modBus);
        ModRecipeSerializers.register(modBus);
    }
}
