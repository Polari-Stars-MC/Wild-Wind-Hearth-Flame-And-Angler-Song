package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.block.ModTerrainBlocks;
import org.polaris2023.wwhfas.entity.animal.CrabVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * 模组自定义注册表与延迟注册入口的集中管理类喵~
 */
public final class ModRegistries {
	/**
	 * 螃蟹变种注册表喵~
	 */
	public static final Registry<CrabVariant> CRAB_VARIANTS = new RegistryBuilder<>(Keys.CRAB_VARIANT)
			.sync(true)
			.defaultKey(WildWindMod.id("temperate"))
			.create();

	/**
	 * 注册模组内全部延迟注册内容喵~
	 *
	 * @param modEventBus 模组事件总线喵~
	 */
	public static void registerAllEntries(IEventBus modEventBus) {
		ModBlocks.register(modEventBus);
		ModTerrainBlocks.register(modEventBus);
		ModItems.register(modEventBus);
		ModCreativeTabs.register(modEventBus);
		ModSounds.register(modEventBus);
		ModMenuTypes.register(modEventBus);
		ModDataComponents.register(modEventBus);
		ModDataAttachments.register(modEventBus);
		ModBlockEntities.register(modEventBus);
		ModEntities.register(modEventBus);
		ModParticleTypes.register(modEventBus);
		ModRecipeTypes.register(modEventBus);
		ModRecipeSerializers.register(modEventBus);
		ModFoliagePlacerTypes.FOLIAGE_PLACER_TYPE.register(modEventBus);
		ModMobEffects.register(modEventBus);
		ModPotions.register(modEventBus);
		ModEntityDataSerializers.register(modEventBus);
		ModCrabVariants.register(modEventBus);
		ModAttributes.ATTRIBUTES.register(modEventBus);
	}

	/**
	 * 模组自定义注册表键定义喵~
	 */
	public interface Keys {
		/**
		 * 螃蟹变种注册表的资源键喵~
		 */
		ResourceKey<Registry<CrabVariant>> CRAB_VARIANT = ResourceKey.createRegistryKey(WildWindMod.id("crab_variant"));
	}
}
