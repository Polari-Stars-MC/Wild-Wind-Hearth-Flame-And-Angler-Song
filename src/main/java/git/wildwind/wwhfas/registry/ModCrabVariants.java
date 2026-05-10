package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.animal.CrabVariant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCrabVariants {
    public static final DeferredRegister<CrabVariant> CRAB_VARIANTS = DeferredRegister.create(ModRegistries.CRAB_VARIANT, WildWindMod.MOD_ID);

    public static final DeferredHolder<CrabVariant, CrabVariant> TEMPERATE = register("temperate");
    public static final DeferredHolder<CrabVariant, CrabVariant> COLD = register("cold");
    public static final DeferredHolder<CrabVariant, CrabVariant> WARM = register("warm");

    public static void register(IEventBus modBus) {
        CRAB_VARIANTS.register(modBus);
    }

    private static DeferredHolder<CrabVariant, CrabVariant> register(String path) {
        return CRAB_VARIANTS.register(path, () -> new CrabVariant(WildWindMod.id("textures/entity/crab/" + path + ".png")));
    }
}
