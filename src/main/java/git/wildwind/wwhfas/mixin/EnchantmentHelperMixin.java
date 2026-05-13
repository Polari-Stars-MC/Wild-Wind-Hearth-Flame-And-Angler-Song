package git.wildwind.wwhfas.mixin;

import git.wildwind.wwhfas.item.OmniClawItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @ModifyVariable(method = "getRandomItemWith", at = @At(value = "STORE", ordinal = 0))
    private static ItemStack getRandomItemWithUnwarpOmniClaw(ItemStack stack) {
        if (stack.getItem() instanceof OmniClawItem omniClaw) {
            return omniClaw.getLastSelectedTool(stack);
        }

        return stack;
    }
}
