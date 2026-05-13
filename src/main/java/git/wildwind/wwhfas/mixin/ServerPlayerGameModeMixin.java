package git.wildwind.wwhfas.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import git.wildwind.wwhfas.item.OmniClawItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @ModifyVariable(method = "destroyBlock", at = @At(value = "STORE", ordinal = 0))
    private ItemStack destroyBlockUnwarpOmniClaw(ItemStack stack, @Local(ordinal = 1) BlockState state) {
        if (stack.getItem() instanceof OmniClawItem omniClaw) {
            return omniClaw.getToolFor(stack, state);
        }

        return stack;
    }

    @WrapOperation(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack handleBlockBreakUnwarpOmniClaw(ServerPlayer instance, Operation<ItemStack> original, @Local BlockState state) {
        ItemStack stack = original.call(instance);
        if (stack.getItem() instanceof OmniClawItem omniClaw) {
            return omniClaw.getToolFor(stack, state);
        }

        return stack;
    }
}
