package org.polaris2023.wwhfas.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.wwhfas.entity.WindupAttackMob;
import org.polaris2023.wwhfas.entity.ai.goal.AlertOthersNearestAttackableTargetGoal;
import org.polaris2023.wwhfas.entity.ai.goal.ChargingMeleeAttackGoal;
import org.polaris2023.wwhfas.registry.ModItems;
import org.polaris2023.wwhfas.tag.ModBiomeTags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Piranha extends AbstractSchoolingFish implements WindupAttackMob, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int preparingToAttack;

    public Piranha(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.95)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public static boolean checkPiranhaSpawnRules(
            EntityType<Piranha> piranha, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return level.getFluidState(pos.below()).is(FluidTags.WATER)
                && level.getBlockState(pos.above()).is(Blocks.WATER)
                && (
                level.getBiome(pos).is(ModBiomeTags.ALLOWS_PIRANHA_SPAWNS_AT_ANY_HEIGHT)
                        || WaterAnimal.checkSurfaceWaterAnimalSpawnRules(piranha, level, spawnType, pos, random)
        );
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PiranhaAttackGoal(this, 2.0f, false));
        this.goalSelector.addGoal(4, new AbstractFish.FishSwimGoal(this));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Piranha.class).setAlertOthers());
        this.targetSelector.addGoal(1, new AlertOthersNearestAttackableTargetGoal<>(this, Mob.class, true,
                entity -> !(entity instanceof Piranha) && (entity.getHealth() <= entity.getMaxHealth() * 0.25))
                .overrideFindEntityFollowDistance(15.0)
        );
        this.targetSelector.addGoal(1, new AlertOthersNearestAttackableTargetGoal<>(this, Player.class, true,
                entity -> entity.getHealth() <= entity.getMaxHealth() * 0.25)
                .overrideFindEntityFollowDistance(15.0)
        );
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    // WIP SOUND
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack((ItemLike) ModItems.PIRANHA_BUCKET);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle", state ->
                this.isInWater()
                        ? state.setAndContinue(DefaultAnimations.SWIM)
                        : state.setAndContinue(DefaultAnimations.IDLE)
        ));
        controllers.add(new AnimationController<>(this, "Attack", state -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void setPreparingToAttackTick(int tick) {
        this.preparingToAttack = tick;
    }

    @Override
    public int getPreparingToAttackTick() {
        return this.preparingToAttack;
    }

    @Override
    public int getPreparationToAttackDuration() {
        return 6;
    }

    @Override
    public boolean prepareAttack(LivingEntity entity) {
        boolean result = WindupAttackMob.super.prepareAttack(entity);
        if (result) this.triggerAnim("Attack", "attack");
        return result;
    }

    @Override
    public boolean canContinuePreparingAttack() {
        return this.isInWater();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.isAlive()) {
            processPreparingAttack(this);
        }
    }

    protected static class PiranhaAttackGoal extends ChargingMeleeAttackGoal<Piranha> {
        public PiranhaAttackGoal(Piranha mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return this.mob.isInWater() && super.canUse();
        }
    }
}
