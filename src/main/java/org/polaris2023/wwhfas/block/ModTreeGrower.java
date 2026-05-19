package org.polaris2023.wwhfas.block;

import org.polaris2023.wwhfas.datagen.provider.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/**
 * 模组树木生长器常量喵~
 */
public final class ModTreeGrower {
	/**
	 * 焦烬树生长器喵~
	 */
	public static final TreeGrower CINDER = new TreeGrower(
		"cinder",
		0.0F,
		Optional.empty(),
		Optional.empty(),
		Optional.of(ModConfiguredFeatures.CINDER),
		Optional.empty(),
		Optional.empty(),
		Optional.empty()
	);

	/**
	 * 余烬树生长器喵~
	 */
	public static final TreeGrower EMBER = new TreeGrower(
		"ember",
		0.0F,
			Optional.empty(),
			Optional.empty(),
		Optional.of(ModConfiguredFeatures.EMBER),
		Optional.empty(),
		Optional.empty(),
		Optional.empty()
	);

	private ModTreeGrower() {
	}
}
