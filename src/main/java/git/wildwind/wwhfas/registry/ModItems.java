package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, WildWindMod.MOD_ID);

    public static final WoodItems CINDER = registerWoodItems(ModBlocks.CINDER, ModBoatTypes.CINDER);
    public static final WoodItems EMBER = registerWoodItems(ModBlocks.EMBER, ModBoatTypes.EMBER);

    public static final List<WoodItems> WOOD_ITEMS = List.of(CINDER, EMBER);

    private ModItems() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

        private static WoodItems registerWoodItems(ModBlocks.WoodSet woodSet, EnumProxy<Boat.Type> boatType) {
        DeferredHolder<Item, Item> log = blockItem(woodSet.log());
        DeferredHolder<Item, Item> wood = blockItem(woodSet.wood());
        DeferredHolder<Item, Item> strippedLog = blockItem(woodSet.strippedLog());
        DeferredHolder<Item, Item> strippedWood = blockItem(woodSet.strippedWood());
        DeferredHolder<Item, Item> leaves = blockItem(woodSet.leaves());
        DeferredHolder<Item, Item> planks = blockItem(woodSet.planks());
        DeferredHolder<Item, Item> stairs = blockItem(woodSet.stairs());
        DeferredHolder<Item, Item> slab = blockItem(woodSet.slab());
        DeferredHolder<Item, Item> fence = blockItem(woodSet.fence());
        DeferredHolder<Item, Item> fenceGate = blockItem(woodSet.fenceGate());
        DeferredHolder<Item, Item> door = ITEMS.register(
            woodSet.door().getId().getPath(),
            () -> new BlockItem(woodSet.door().get(), new Item.Properties())
        );
        DeferredHolder<Item, Item> trapdoor = blockItem(woodSet.trapdoor());
        DeferredHolder<Item, Item> pressurePlate = blockItem(woodSet.pressurePlate());
        DeferredHolder<Item, Item> button = blockItem(woodSet.button());
        DeferredHolder<Item, Item> sapling = blockItem(woodSet.sapling());
        DeferredHolder<Item, Item> sign = ITEMS.register(
            woodSet.name() + "_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), woodSet.sign().get(), woodSet.wallSign().get())
        );
        DeferredHolder<Item, Item> hangingSign = ITEMS.register(
            woodSet.name() + "_hanging_sign",
            () -> new HangingSignItem(
                woodSet.hangingSign().get(),
                woodSet.wallHangingSign().get(),
                new Item.Properties().stacksTo(16)
            )
        );
        DeferredHolder<Item, Item> boat = ITEMS.register(
            woodSet.name() + "_boat",
            () -> new BoatItem(false, boatType.getValue(), new Item.Properties().stacksTo(1))
        );
        DeferredHolder<Item, Item> chestBoat = ITEMS.register(
            woodSet.name() + "_chest_boat",
            () -> new BoatItem(true, boatType.getValue(), new Item.Properties().stacksTo(1))
        );

        return new WoodItems(
            woodSet.name(),
            log,
            wood,
            strippedLog,
            strippedWood,
            leaves,
            planks,
            stairs,
            slab,
            fence,
            fenceGate,
            door,
            trapdoor,
            pressurePlate,
            button,
            sapling,
            sign,
            hangingSign,
            boat,
            chestBoat
        );
    }

    private static DeferredHolder<Item, Item> blockItem(DeferredHolder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public record WoodItems(
        String name,
        DeferredHolder<Item, Item> log,
        DeferredHolder<Item, Item> wood,
        DeferredHolder<Item, Item> strippedLog,
        DeferredHolder<Item, Item> strippedWood,
        DeferredHolder<Item, Item> leaves,
        DeferredHolder<Item, Item> planks,
        DeferredHolder<Item, Item> stairs,
        DeferredHolder<Item, Item> slab,
        DeferredHolder<Item, Item> fence,
        DeferredHolder<Item, Item> fenceGate,
        DeferredHolder<Item, Item> door,
        DeferredHolder<Item, Item> trapdoor,
        DeferredHolder<Item, Item> pressurePlate,
        DeferredHolder<Item, Item> button,
        DeferredHolder<Item, Item> sapling,
        DeferredHolder<Item, Item> sign,
        DeferredHolder<Item, Item> hangingSign,
        DeferredHolder<Item, Item> boat,
        DeferredHolder<Item, Item> chestBoat
    ) {
    }
}
