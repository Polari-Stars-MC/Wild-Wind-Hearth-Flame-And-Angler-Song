package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = WildWindMod.MOD_ID)
public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, WildWindMod.MOD_ID);

    public static final DeferredHolder<Potion, Potion> REACH =
            POTIONS.register("reach", () -> new Potion(new MobEffectInstance(ModMobEffects.REACH, 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_REACH =
            POTIONS.register("long_reach", () -> new Potion("reach", new MobEffectInstance(ModMobEffects.REACH, 9600)));

    public static void register(IEventBus modBus) {
        POTIONS.register(modBus);
    }

    @SubscribeEvent
    static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(
                Potions.AWKWARD,
                ModItems.CRAB_CLAW.get(),
                ModPotions.REACH
        );

        builder.addMix(
                ModPotions.REACH,
                Items.REDSTONE,
                ModPotions.LONG_REACH
        );
    }
}
