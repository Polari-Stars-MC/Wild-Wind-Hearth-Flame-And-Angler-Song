package org.polaris2023.wild_wind.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wild_wind.WildWindMod;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, WildWindMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PIRANHA_ATTACK = SOUND_EVENTS.register(
            "entity.piranha.attack",
            SoundEvent::createVariableRangeEvent
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> PIRANHA_DEATH = SOUND_EVENTS.register(
            "entity.piranha.death",
            SoundEvent::createVariableRangeEvent
    );

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }

    public static String subtitleOf(DeferredHolder<SoundEvent, SoundEvent> sound) {
        ResourceLocation id = sound.getId();
        return sound + id.getNamespace() + id.getPath();
    }
}
