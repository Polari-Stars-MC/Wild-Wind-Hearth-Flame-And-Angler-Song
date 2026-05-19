package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组声音事件喵~
 */
public final class ModSounds {
	/**
	 * 声音事件延迟注册器喵~
	 */
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(Registries.SOUND_EVENT, WildWindMod.MOD_ID);

	private ModSounds() {
	}

	/**
	 * 向模组事件总线注册声音事件喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		SOUND_EVENTS.register(modBus);
	}
}
