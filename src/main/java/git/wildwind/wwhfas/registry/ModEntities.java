package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.Crab;
import git.wildwind.wwhfas.entity.render.CrabRenderer;
import git.wildwind.wwhfas.entity.render.model.CrabModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WildWindMod.MOD_ID);

    private ModEntities() {
    }

    public static final DeferredHolder<EntityType<?>, EntityType<Crab>> CRAB =
            ENTITY_TYPES.register("crab",
                    () -> EntityType.Builder.of(Crab::new, MobCategory.CREATURE)
                            .sized(0.5F, 0.55F)
                            .clientTrackingRange(10)
                            .build("crab"));

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }


    @EventBusSubscriber(value = Dist.CLIENT)
    public static class EntitiesClientEvent {

        @SubscribeEvent
        static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CrabModel.LAYER_LOCATION, CrabModel::createBodyLayer);
        }

        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.CRAB.get(),CrabRenderer::new);
        }
    }

    @EventBusSubscriber
    public static class EntitiesSeverEvent {

        @SubscribeEvent
        static void onAttributeCreate(EntityAttributeCreationEvent event) {
            event.put(ModEntities.CRAB.get(), Crab.createAttributes().build());
        }

        @SubscribeEvent
        static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
            event.register(CRAB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        }
    }
}
