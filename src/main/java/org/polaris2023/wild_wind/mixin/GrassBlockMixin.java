package org.polaris2023.wild_wind.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.wild_wind.datagen.provider.ModPlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

/**
 * 若草方块在沼泽生物群系，则将其产生的地物替换为wild_wind:grass_swamp_bonemeal
 */
@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin {

	@ModifyVariable(method = "performBonemeal", at = @At("STORE"))
	private Optional<Holder.Reference<PlacedFeature>> modifyPlacedFeature(Optional<Holder.Reference<PlacedFeature>> original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) BlockPos pos) {
		if (level.getBiome(pos).is(Tags.Biomes.IS_SWAMP)) {
			for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-2, -1, -2), pos.offset(2, 1, 2))) {
				if (level.getBlockState(blockPos).is(Blocks.WATER)) return level.registryAccess()
						.registryOrThrow(Registries.PLACED_FEATURE)
						.getHolder(ModPlacedFeatures.GRASS_SWAMP_BONEMEAL);
			}
		}

		return original;
	}
}
