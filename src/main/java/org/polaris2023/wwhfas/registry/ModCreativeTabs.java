package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组创造模式物品栏喵~
 */
public final class ModCreativeTabs {
	/**
	 * 创造模式物品栏延迟注册器喵~
	 */
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WildWindMod.MOD_ID);
	/**
	 * 模组专用创造模式物品栏喵~
	 */
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WILD_WIND =
			CREATIVE_TABS.register("wild_wind", () -> CreativeModeTab.builder()
					.title(Component.translatable("mod.wwhfas.name"))
					.icon(() -> ModItems.CINDER.log().get().getDefaultInstance())
					.displayItems((params, output) -> populateModTab(output))
					.build()
			);

	/**
	 * 向模组事件总线注册创造模式物品栏喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		CREATIVE_TABS.register(modBus);
	}

	private static void populateModTab(CreativeModeTab.Output output) {
		addWoodBuildingSet(output, ModItems.AZALEA);
		addWoodBuildingSet(output, ModItems.CINDER);
		addWoodBuildingSet(output, ModItems.EMBER);

		accept(output, ModItems.AZALEA.log());
		accept(output, ModItems.CINDER.log());
		accept(output, ModItems.EMBER.log());

		accept(output, ModItems.CINDER.leaves());
		accept(output, ModItems.EMBER.leaves());

		accept(output, ModItems.CINDER.sapling());
		accept(output, ModItems.EMBER.sapling());

		accept(output, ModItems.SCORCHED_GRASS);
		accept(output, ModItems.SCORCHED_TWIG);
		accept(output, ModItems.SCORCHED_GRASS_BLOCK);
		accept(output, ModItems.SCORCHED_DIRT);

		output.accept(ModItems.CRAB_BUCKET.get().getDefaultInstance());
		output.accept(ModItems.CRAB_CLAW.get().getDefaultInstance());
		output.accept(ModItems.OMNI_CLAW.get().getDefaultInstance());
		output.accept(ModItems.CRAB_SPAWN_EGG.get().getDefaultInstance());

		output.accept(ModItems.REEDS.get());
		output.accept(ModItems.CATTAILS.get());

		output.accept(ModItems.PIRANHA_BUCKET);
		output.accept(ModItems.PIRANHA);
		output.accept(ModItems.COOKED_PIRANHA);
		output.accept(ModItems.FANG);
		output.accept(ModItems.PIRANHA_SPAWN_EGG);
	}

	private static void addWoodBuildingSet(CreativeModeTab.Output output, ModItems.WoodItems woodItems) {
		accept(output, woodItems.log());
		accept(output, woodItems.wood());
		accept(output, woodItems.strippedLog());
		accept(output, woodItems.strippedWood());
		accept(output, woodItems.planks());
		accept(output, woodItems.stairs());
		accept(output, woodItems.slab());
		accept(output, woodItems.fence());
		accept(output, woodItems.fenceGate());
		accept(output, woodItems.door());
		accept(output, woodItems.trapdoor());
		accept(output, woodItems.pressurePlate());
		accept(output, woodItems.button());
	}

	private static void accept(CreativeModeTab.Output output, DeferredHolder<Item, Item> item) {
		if (item != null) {
			output.accept(item.get().getDefaultInstance());
		}
	}

	private ModCreativeTabs() {
	}
}
