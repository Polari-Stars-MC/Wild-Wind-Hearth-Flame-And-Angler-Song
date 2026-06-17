package org.polaris2023.wwhfas.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.polaris2023.wwhfas.entity.WindupAttackMob;
import org.polaris2023.wwhfas.entity.ai.goal.ChargingMeleeAttackGoal;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Spiderling extends Spider implements GeoEntity, WindupAttackMob {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final int WINDUP_ATTACK_COOLDOWN = 5;
    private int windupAttackCooldown;
    private int preparingToAttackTicks;

    public Spiderling(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> goal instanceof MeleeAttackGoal);
        this.goalSelector.addGoal(4, new SpiderlingAttackGoal(this, 1.0f, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Move", 2, this::moveAnimController));
        controllers.add(new AnimationController<>(this, "Attack", 0, state -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING)
        );
    }

    protected PlayState moveAnimController(final AnimationState<Spiderling> state) {
        return state.isMoving() ?
                state.setAndContinue(DefaultAnimations.WALK)
                : PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected boolean isImmobile() {
        return !this.isPreparingToAttack() && super.isImmobile();
    }

    @Override
    public boolean onClimbable() {
        return !this.isPreparingToAttack() && super.onClimbable();
    }

    @Override
    public void setPreparingToAttackTick(int tick) {
        this.preparingToAttackTicks = tick;
    }

    @Override
    public int getPreparingToAttackTick() {
        return this.preparingToAttackTicks;
    }

    @Override
    public int getPreparationToAttackDuration() {
        return 13;
    }

    @Override
    public boolean prepareAttack(LivingEntity entity) {
        boolean result = WindupAttackMob.super.prepareAttack(entity);
        if (result) this.triggerAnim("Attack", "attack");
        return result;
    }

    @Override
    public void onAttackDone(LivingEntity target) {
        this.windupAttackCooldown = WINDUP_ATTACK_COOLDOWN;
        Vec3 selfPos = this.getPosition(1.0f);
        Vec3 targetPos = target.getPosition(1.0f);
        Vec3 velocity = targetPos.subtract(selfPos)
                .normalize()
                .multiply(0.7, 0.7, 0.7)
                .add(0.0, 0.45, 0.0);

        this.addDeltaMovement(velocity);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            this.processPreparingAttack(this);
            if (this.windupAttackCooldown > 0) this.windupAttackCooldown--;
        }
    }

    protected static class SpiderlingAttackGoal extends ChargingMeleeAttackGoal<Spiderling> {
        public SpiderlingAttackGoal(Spiderling mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return this.mob.onGround() && super.canUse() && !this.mob.isVehicle();
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }
    }
}
