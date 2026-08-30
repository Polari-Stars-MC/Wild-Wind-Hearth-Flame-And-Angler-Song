package org.polaris2023.wild_wind;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.polaris2023.wild_wind.config.ModConfigs;
import org.polaris2023.wild_wind.datagen.ModDataGen;
import org.polaris2023.wild_wind.loot.LootTableModifications;
import org.polaris2023.wild_wind.registry.ModCommonSetup;
import org.polaris2023.wild_wind.registry.ModRegistries;
import org.slf4j.Logger;

/**
 * 模组主入口，负责初始化注册表、通用事件、数据生成与配置喵~
 */
@Mod(WildWindMod.MOD_ID)
public class WildWindMod {
	/**
	 * 模组唯一标识符喵~
	 */
	public static final String MOD_ID = "wild_wind";
	/**
	 * 模组通用日志记录器喵~
	 */
	public static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * 创建模组主实例并完成启动期注册喵~
	 *
	 * @param modEventBus 模组事件总线喵~
	 * @param modContainer 当前模组容器喵~
	 */
	public WildWindMod(IEventBus modEventBus, ModContainer modContainer) {
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::registerRegistries);
		ModRegistries.registerAllEntries(modEventBus);
		ModCommonSetup.register(modEventBus);
		ModDataGen.register(modEventBus);
		ModConfigs.register(modContainer);
	}

	@SubscribeEvent
	void modifyLoots(LootTableLoadEvent event) {
		if (!LootTableModifications.hasModifications()) return;
		LootTableModifications.applyModification(event.getKey(), event.getTable(), event.getRegistries());
	}

	void registerRegistries(NewRegistryEvent event) {
		event.register(ModRegistries.MUDCRAB_VARIANTS);
	}

	/**
	 * 创建当前模组命名空间下的资源定位符喵~
	 *
	 * @param path 资源路径喵~
	 * @return 对应的资源定位符喵~
	 */
	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
