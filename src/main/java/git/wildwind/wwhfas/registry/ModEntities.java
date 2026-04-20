package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.Crab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WildWindMod.MOD_ID);

    private ModEntities() {
    }

    public static final DeferredHolder<EntityType<?>, EntityType<Crab>> CRAB =
            ENTITY_TYPES.register("crab",
                    () -> EntityType.Builder.of(Crab::new, MobCategory.AMBIENT)
                            .sized(0.6F, 1.8F)
                            .build("crab"));

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }
}
