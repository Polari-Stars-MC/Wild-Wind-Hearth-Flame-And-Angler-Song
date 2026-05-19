package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组粒子类型喵~
 */
public final class ModParticleTypes {
    /**
     * 粒子类型延迟注册器喵~
     */
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, WildWindMod.MOD_ID);

    private ModParticleTypes() {
    }

    /**
     * 向模组事件总线注册粒子类型喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        PARTICLE_TYPES.register(modBus);
    }
}
