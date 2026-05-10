package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.effect.WildWindMobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, WildWindMod.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> REACH =
            MOB_EFFECTS.register("reach",
                    () -> new WildWindMobEffect(MobEffectCategory.BENEFICIAL, 0xFF3F7E8E)
                            .addAttributeModifier(
                                    Attributes.BLOCK_INTERACTION_RANGE,
                                    WildWindMod.id("effect.reach"),
                                    2.0,
                                    AttributeModifier.Operation.ADD_VALUE
                            )
                            .addAttributeModifier(
                                    Attributes.ENTITY_INTERACTION_RANGE,
                                    WildWindMod.id("effect.reach"),
                                    2.0,
                                    AttributeModifier.Operation.ADD_VALUE
                            )
                            .addAttributeModifier(
                                    ModAttributes.EXTRA_ITEM_PICKUP_RANGE,
                                    WildWindMod.id("effect.reach"),
                                    2.0,
                                    AttributeModifier.Operation.ADD_VALUE
                            )
            );

    public static void register(IEventBus modBus) {
        MOB_EFFECTS.register(modBus);
    }
}
