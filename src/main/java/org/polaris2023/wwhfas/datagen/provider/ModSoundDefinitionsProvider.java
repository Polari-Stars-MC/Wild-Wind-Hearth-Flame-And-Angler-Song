package org.polaris2023.wwhfas.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinition.Sound;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.registry.ModSoundEvents;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, WildWindMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.PIRANHA_ATTACK, SoundDefinition.definition()
                .with(this.sounds(ModSoundEvents.PIRANHA_ATTACK, 3, 0.4))
                .subtitle(ModSoundEvents.subtitleOf(ModSoundEvents.PIRANHA_ATTACK))
                .replace(true)
        );

        add(ModSoundEvents.PIRANHA_DEATH, SoundDefinition.definition()
                .with(this.sounds(ModSoundEvents.PIRANHA_DEATH, 4, 0.4))
                .subtitle(ModSoundEvents.subtitleOf(ModSoundEvents.PIRANHA_DEATH))
                .replace(true)
        );

        add(ModSoundEvents.PIRANHA_HURT, SoundDefinition.definition()
                .with(this.sounds(ModSoundEvents.PIRANHA_HURT, 4, 0.4))
                .subtitle(ModSoundEvents.subtitleOf(ModSoundEvents.PIRANHA_DEATH))
                .replace(true)
        );
    }

    private Sound sound(DeferredHolder<SoundEvent, SoundEvent> soundEvent) {
        return sound(soundEvent.getId().toString().replace('.', '_'));
    }

    private Sound[] sounds(DeferredHolder<SoundEvent, SoundEvent> soundEvent, int count) {
        return this.sounds(soundEvent, count, 1.0f);
    }

    private Sound[] sounds(DeferredHolder<SoundEvent, SoundEvent> soundEvent, int count, double volume) {
        return this.sounds(soundEvent, count, volume, 1.0f);
    }

    private Sound[] sounds(DeferredHolder<SoundEvent, SoundEvent> soundEvent, int count, double volume, double pitch) {
        Sound[] sounds = new Sound[count];
        for (int i = 0; i < count; i++) {
            sounds[i] = sound(soundEvent.getId().toString().replace('.', '_') + "_" + (i + 1))
                    .volume(volume)
                    .pitch(pitch);
        }

        return sounds;
    }
}
