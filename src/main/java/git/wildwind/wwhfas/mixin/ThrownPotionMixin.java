package git.wildwind.wwhfas.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import git.wildwind.wwhfas.entity.animal.Crab;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
