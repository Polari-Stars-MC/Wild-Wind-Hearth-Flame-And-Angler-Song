package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.animal.CrabVariant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册螃蟹变种喵~
 */
public class ModCrabVariants {
    /**
     * 螃蟹变种延迟注册器喵~
     */
    public static final DeferredRegister<CrabVariant> CRAB_VARIANTS = DeferredRegister.create(ModRegistries.CRAB_VARIANTS, WildWindMod.MOD_ID);

    /**
     * 温带螃蟹变种喵~
     */
    public static final DeferredHolder<CrabVariant, CrabVariant> TEMPERATE = register("temperate");
    /**
     * 寒带螃蟹变种喵~
     */
    public static final DeferredHolder<CrabVariant, CrabVariant> COLD = register("cold");
    /**
     * 暖带螃蟹变种喵~
     */
    public static final DeferredHolder<CrabVariant, CrabVariant> WARM = register("warm");

    /**
     * 向模组事件总线注册螃蟹变种喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        CRAB_VARIANTS.register(modBus);
    }

    private static DeferredHolder<CrabVariant, CrabVariant> register(String path) {
        return CRAB_VARIANTS.register(path, () -> new CrabVariant(WildWindMod.id("textures/entity/crab/" + path + ".png")));
    }

	private ModCrabVariants() {
	}
}
