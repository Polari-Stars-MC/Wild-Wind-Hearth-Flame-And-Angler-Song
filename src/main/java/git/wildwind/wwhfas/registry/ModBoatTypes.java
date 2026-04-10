package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import java.util.function.Supplier;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public final class ModBoatTypes {
    public static final EnumProxy<Boat.Type> CINDER = new EnumProxy<>(
            Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.CINDER.planks().get(),
            WildWindMod.MOD_ID + ":cinder",
            (Supplier<Item>) () -> ModItems.CINDER.boat().get(),
            (Supplier<Item>) () -> ModItems.CINDER.chestBoat().get(),
            (Supplier<Item>) () -> Items.STICK,
            false
    );
    public static final EnumProxy<Boat.Type> EMBER = new EnumProxy<>(
            Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.EMBER.planks().get(),
            WildWindMod.MOD_ID + ":ember",
            (Supplier<Item>) () -> ModItems.EMBER.boat().get(),
            (Supplier<Item>) () -> ModItems.EMBER.chestBoat().get(),
            (Supplier<Item>) () -> Items.STICK,
            false
    );

    private ModBoatTypes() {
    }
}