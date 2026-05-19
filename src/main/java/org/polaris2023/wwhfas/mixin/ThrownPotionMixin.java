package org.polaris2023.wwhfas.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import org.polaris2023.wwhfas.entity.animal.Crab;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 为水瓶泼洒效果补充螃蟹复水逻辑的混入类喵~
 */
@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin {

	@Inject(method = "applyWater", at = @At("TAIL"))
	private void applyWaterForCrab(CallbackInfo ci, @Local AABB waterArea) {
		Level level = ((ThrownPotion) (Object) this).level();
		for (Crab crab : level.getEntitiesOfClass(Crab.class, waterArea)) {
			crab.rehydrate();
		}
	}
}
