package git.wildwind.wwhfas.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 用于拓宽 {@link MobEffect} 构造方法的访问修饰符
 */
public class WildWindMobEffect extends MobEffect {
    public WildWindMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public WildWindMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }
}
