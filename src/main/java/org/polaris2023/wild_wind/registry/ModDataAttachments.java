package org.polaris2023.wild_wind.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wild_wind.WildWindMod;

/**
 * 注册模组数据附件类型喵~
 */
public final class ModDataAttachments {
	/**
	 * 数据附件类型延迟注册器喵~
	 */
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WildWindMod.MOD_ID);

	private ModDataAttachments() {
	}

	/**
	 * 向模组事件总线注册数据附件类型喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		ATTACHMENT_TYPES.register(modBus);
	}
}
