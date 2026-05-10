package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import git.wildwind.wwhfas.entity.animal.CrabVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class ModRegistries {
    public static final Registry<CrabVariant> CRAB_VARIANT = new RegistryBuilder<>(Keys.CRAB_VARIANT)
            .sync(true)
            .defaultKey(WildWindMod.id("temperate"))
            .create();

    public static void registerAllEntries(IEventBus modBus) {
        ModBlocks.register(modBus);
        ModTerrainBlocks.register(modBus);
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
        ModSounds.register(modBus);
        ModMenuTypes.register(modBus);
        ModDataComponents.register(modBus);
        ModDataAttachments.register(modBus);
        ModBlockEntities.register(modBus);
        ModEntities.register(modBus);
        ModParticleTypes.register(modBus);
        ModRecipeTypes.register(modBus);
        ModRecipeSerializers.register(modBus);
        ModFoliagePlacerTypes.FOLIAGE_PLACER_TYPE.register(modBus);
        ModMobEffects.register(modBus);
        ModPotions.register(modBus);
        ModEntityDataSerializers.register(modBus);
        ModCrabVariants.register(modBus);
    }

    public interface Keys {
        ResourceKey<Registry<CrabVariant>> CRAB_VARIANT = ResourceKey.createRegistryKey(WildWindMod.id("crab_variant"));
    }
}
