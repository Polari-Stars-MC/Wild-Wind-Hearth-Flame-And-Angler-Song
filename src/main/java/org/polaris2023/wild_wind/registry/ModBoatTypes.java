package org.polaris2023.wild_wind.registry;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import org.polaris2023.wild_wind.WildWindMod;

import java.util.function.Supplier;

/**
 * 定义模组船类型枚举扩展喵~
 */
public final class ModBoatTypes {
	/**
	 * 灵焰木船类型喵~
	 */
	public static final EnumProxy<Boat.Type> CINDER = new EnumProxy<>(
			Boat.Type.class,
			(Supplier<Block>) () -> ModBlocks.CINDER.planks().get(),
			WildWindMod.MOD_ID + ":cinder",
			(Supplier<Item>) () -> ModItems.CINDER.boat().get(),
			(Supplier<Item>) () -> ModItems.CINDER.chestBoat().get(),
			(Supplier<Item>) () -> Items.STICK,
			false
	);
	/**
	 * 焚烬木船类型喵~
	 */
	public static final EnumProxy<Boat.Type> EMBER = new EnumProxy<>(
			Boat.Type.class,
			(Supplier<Block>) () -> ModBlocks.EMBER.planks().get(),
			WildWindMod.MOD_ID + ":ember",
			(Supplier<Item>) () -> ModItems.EMBER.boat().get(),
			(Supplier<Item>) () -> ModItems.EMBER.chestBoat().get(),
			(Supplier<Item>) () -> Items.STICK,
			false
	);
	/**
	 * 杜鹃木船类型喵~
	 */
	public static final EnumProxy<Boat.Type> AZALEA = new EnumProxy<>(
			Boat.Type.class,
			(Supplier<Block>) () -> ModBlocks.AZALEA.planks().get(),
			WildWindMod.MOD_ID + ":azalea",
			(Supplier<Item>) () -> ModItems.AZALEA.boat().get(),
			(Supplier<Item>) () -> ModItems.AZALEA.chestBoat().get(),
			(Supplier<Item>) () -> Items.STICK,
			false
	);

	private ModBoatTypes() {
	}
}
