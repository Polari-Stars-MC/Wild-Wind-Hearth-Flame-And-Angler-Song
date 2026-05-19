package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.client.entity.render.CrabRenderer;
import git.wildwind.wwhfas.entity.animal.Crab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
	 * 螃蟹实体类型喵~
	 */
	public static final DeferredHolder<EntityType<?>, EntityType<Crab>> CRAB =
			ENTITY_TYPES.register("crab",
					() -> EntityType.Builder.of(Crab::new, MobCategory.WATER_CREATURE)
							.sized(0.5F, 0.55F)
							.clientTrackingRange(10)
							.build("crab"));

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
			event.registerEntityRenderer(ModEntities.CRAB.get(), CrabRenderer::new);
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
			event.put(ModEntities.CRAB.get(), Crab.createAttributes().build());
		}

		@SubscribeEvent
		static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
			event.register(CRAB.get(), Crab.SPAWN_PLACEMENT, Heightmap.Types.OCEAN_FLOOR, Crab::checkCrabInWaterGroundSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
			event.register(CRAB.get(), Crab::checkCrabOnGroundSpawnRules);
		}

		private EntitiesSeverEvent() {
		}
	}
}
