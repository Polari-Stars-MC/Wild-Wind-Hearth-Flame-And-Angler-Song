package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, WildWindMod.MOD_ID);

    public static final Supplier<Potion> EXTENSION =
            POTIONS.register("extension", () -> new Potion(new MobEffectInstance(ModMobEffects.EXTENSION, 3600)));

    public static void register(IEventBus modBus) {
        POTIONS.register(modBus);
    }

}
