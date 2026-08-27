package org.polaris2023.wild_wind.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.WaterAnimal;
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
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.client.entity.render.MudcrabRenderer;
import org.polaris2023.wild_wind.client.entity.render.PiranhaRenderer;
import org.polaris2023.wild_wind.entity.animal.Mudcrab;
import org.polaris2023.wild_wind.entity.animal.Piranha;

/**
 * 注册模组实体类型喵~
 */
public final class ModEntities {
	/**
	 * 实体类型延迟注册器喵~
	 */
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(Registries.ENTITY_TYPE, WildWindMod.MOD_ID);

	private ModEntities() {
	}

	/**
	 * 泥沼蟹实体类型喵~
	 */
	public static final DeferredHolder<EntityType<?>, EntityType<Mudcrab>> MUDCRAB =
			ENTITY_TYPES.register("mudcrab",
					() -> EntityType.Builder.of(Mudcrab::new, MobCategory.WATER_CREATURE)
							.sized(0.5F, 0.55F)
							.clientTrackingRange(8)
							.build("mudcrab"));

	public static final DeferredHolder<EntityType<?>, EntityType<Piranha>> PIRANHA =
			ENTITY_TYPES.register("piranha",
					() -> EntityType.Builder.of(Piranha::new, MobCategory.WATER_AMBIENT)
							.sized(0.7F, 0.4F)
							.eyeHeight(0.26F)
							.build("piranha")
			);

	/**
	 * 向模组事件总线注册实体类型喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		ENTITY_TYPES.register(modBus);
	}

	/**
	 * 实体客户端事件处理器
	 */
	@EventBusSubscriber(value = Dist.CLIENT)
	public static class EntitiesClientEvent {
		@SubscribeEvent
		static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(ModEntities.MUDCRAB.get(), MudcrabRenderer::new);
			event.registerEntityRenderer(ModEntities.PIRANHA.get(), PiranhaRenderer::new);
		}

		private EntitiesClientEvent() {
		}
	}

	/**
	 * 实体服务端事件处理器
	 */
	@EventBusSubscriber
	public static class EntitiesSeverEvent {

		@SubscribeEvent
		static void onAttributeCreate(EntityAttributeCreationEvent event) {
			event.put(ModEntities.MUDCRAB.get(), Mudcrab.createAttributes().build());
			event.put(ModEntities.PIRANHA.get(), Piranha.createAttributes().build());
		}

		@SubscribeEvent
		static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
			event.register(MUDCRAB.get(), Mudcrab.SPAWN_PLACEMENT, Heightmap.Types.OCEAN_FLOOR, Mudcrab::checkMudcrabInWaterGroundSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
			event.register(MUDCRAB.get(), Mudcrab::checkMudcrabOnGroundSpawnRules);
			event.register(PIRANHA.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		}

		private EntitiesSeverEvent() {
		}
	}
}
