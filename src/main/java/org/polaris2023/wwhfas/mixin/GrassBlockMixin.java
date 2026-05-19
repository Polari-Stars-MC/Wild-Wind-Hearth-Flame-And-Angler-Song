package org.polaris2023.wwhfas.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.polaris2023.wwhfas.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 为草方块施肥生成逻辑注入芦苇替换行为的混入类喵~
 */
@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin {

	@Inject(method = "performBonemeal", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z"))
	private void replaceTallGrassToReeds(ServerLevel level, RandomSource random, BlockPos plantedPos, BlockState blockState, CallbackInfo ci, @Local(ordinal = 1) BlockPos blockPos) {
		if (level.getBlockState(blockPos).is(Blocks.TALL_GRASS)) wwhfas$tryReplaceToReeds(level, random, blockPos);
	}

	@WrapOperation(method = "performBonemeal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
	private void replaceBonemealShortGrassToReeds(BonemealableBlock instance, ServerLevel level, RandomSource randomSource, BlockPos blockPos, BlockState state, Operation<Void> original) {
		if (state.is(Blocks.SHORT_GRASS) && wwhfas$tryReplaceToReeds(level, randomSource, blockPos)) return;
		original.call(instance, level, randomSource, blockPos, state);
	}

	@Unique
	private static boolean wwhfas$tryReplaceToReeds(ServerLevel level, RandomSource random, BlockPos blockPos) {
		boolean replace = level.getBiome(blockPos).is(Tags.Biomes.IS_SWAMP);
		if (!replace) {
			BlockPos pos1 = blockPos.offset(-2, -2, -2);
			BlockPos pos2 = blockPos.offset(2, 2, 2);
			for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
				if (level.getFluidState(pos).is(Fluids.WATER)) {
					replace = true;
					break;
				}
			}
		}

		if (replace && random.nextFloat() < 0.5f) {
			BlockState state = ModBlocks.REEDS.get().defaultBlockState();
			level.setBlock(blockPos, state, 2);
			ModBlocks.REEDS.get().setPlacedBy(level, blockPos, state, null, ItemStack.EMPTY);
			return true;
		}

		return false;
	}
}
