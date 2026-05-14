package git.wildwind.wwhfas.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import git.wildwind.wwhfas.item.OmniClawItem;
import git.wildwind.wwhfas.item.component.OmniClawTools;
import git.wildwind.wwhfas.registry.ModDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @WrapMethod(method = "isDamageableItem")
    public boolean omniClawIsDamageableItem(Operation<Boolean> original) {
        ItemStack tool = OmniClawItem.getLastSelectedTool((ItemStack) (Object) this);
        if (!tool.isEmpty()) return tool.isDamageableItem();

        return original.call();
    }

    @WrapOperation(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void omniClawShrinkTool(ItemStack instance, int decrement, Operation<Void> original, @Local LocalRef<Item> item) {
        ItemStack tool = OmniClawItem.getLastSelectedTool(instance);
        if (tool.isEmpty()) {
            original.call(instance, decrement);
            return;
        }

        item.set(tool.getItem());
        tool.shrink(decrement);
        OmniClawTools tools = instance.get(ModDataComponents.OMNI_CLAW_TOOLS);
        instance.set(ModDataComponents.OMNI_CLAW_TOOLS, tools.toMutable().scrollSelectToNoEmptyItem().toImmutable());
    }
}
