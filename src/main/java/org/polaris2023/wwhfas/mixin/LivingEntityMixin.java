package org.polaris2023.wwhfas.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.polaris2023.wwhfas.registry.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 为生物基础属性表追加模组属性的混入类喵~
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
	private static AttributeSupplier.Builder addMobAttributes(AttributeSupplier.Builder original) {
		return original.add(ModAttributes.EXTRA_ITEM_PICKUP_RANGE);
	}
}
