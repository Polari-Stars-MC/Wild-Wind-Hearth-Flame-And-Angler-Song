package git.wildwind.wwhfas.entity.ai.goal;

import git.wildwind.wwhfas.entity.Crab;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class CrabAttackGoal extends MeleeAttackGoal {
    private final Crab crab;

    public CrabAttackGoal(Crab crab, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(crab, speedModifier, followingTargetEvenIfNotSeen);
        this.crab = crab;
    }

    @Override
    public boolean canUse() {
        return !this.crab.isPreparingToAttack() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.crab.isPreparingToAttack() && super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (!canPerformAttack(target)) return;
        this.resetAttackCooldown();
        this.crab.prepareAttack(target);
    }
}
