package git.wildwind.wwhfas.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import git.wildwind.wwhfas.item.OmniClawItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 让方块破坏流程识别万用蟹钳当前选中工具的混入类喵~
 */
@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;)V"))
	private void destroyBlockUnwarpOmniClawForLootContext(Block instance, Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, Operation<Void> original) {
		ItemStack clawTool = OmniClawItem.getLastSelectedTool(tool);
		if (!clawTool.isEmpty()) tool = clawTool;

		original.call(instance, level, player, pos, state, blockEntity, tool);
	}

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack destroyBlockOmniClawSelectTool(ServerPlayer instance, Operation<ItemStack> original, @Local(ordinal = 1) BlockState state) {
		ItemStack stack = original.call(instance);
		OmniClawItem.getToolFor(stack, state);
		return stack;
	}

	@WrapOperation(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack handleBlockBreakOmniClawSelectTool(ServerPlayer instance, Operation<ItemStack> original, @Local BlockState state) {
		ItemStack stack = original.call(instance);
		OmniClawItem.getToolFor(stack, state);
		return stack;
	}
}
