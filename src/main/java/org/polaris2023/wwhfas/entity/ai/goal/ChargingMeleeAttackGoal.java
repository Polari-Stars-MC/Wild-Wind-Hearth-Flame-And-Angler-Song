package org.polaris2023.wwhfas.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;
import org.polaris2023.wwhfas.entity.WindupAttackMob;

public class ChargingMeleeAttackGoal<T extends PathfinderMob & WindupAttackMob> extends MeleeAttackGoal {
    protected final T mob;

    public ChargingMeleeAttackGoal(T mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return !this.mob.isPreparingToAttack() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.isPreparingToAttack() && super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (!canPerformAttack(target)) return;
        this.resetAttackCooldown();
        this.mob.prepareAttack(target);
    }
}
