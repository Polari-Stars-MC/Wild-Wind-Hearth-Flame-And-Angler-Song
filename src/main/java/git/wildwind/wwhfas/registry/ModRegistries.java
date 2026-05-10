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
    public static final Registry<CrabVariant> CRAB_VARIANTS = new RegistryBuilder<>(Keys.CRAB_VARIANT)
            .sync(true)
            .defaultKey(WildWindMod.id("temperate"))
            .create();

    public static void registerAllEntries(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModTerrainBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModDataAttachments.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticleTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModFoliagePlacerTypes.FOLIAGE_PLACER_TYPE.register(modEventBus);
        ModMobEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModEntityDataSerializers.register(modEventBus);
        ModCrabVariants.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
    }

    public interface Keys {
        ResourceKey<Registry<CrabVariant>> CRAB_VARIANT = ResourceKey.createRegistryKey(WildWindMod.id("crab_variant"));
    }
}
