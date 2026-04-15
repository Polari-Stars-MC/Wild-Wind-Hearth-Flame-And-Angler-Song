package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WildWindMod.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WILD_WIND =
            CREATIVE_TABS.register("wild_wind", () -> CreativeModeTab.builder()
                    .title(Component.translatable("mod.wwhfas.name"))
                    .icon(() -> ModItems.CINDER.log().get().getDefaultInstance())
                    .displayItems((params, output) -> populateModTab(output))
                    .build()
            );

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
        accept(output, ModItems.TINY_CACTUS);
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
}
