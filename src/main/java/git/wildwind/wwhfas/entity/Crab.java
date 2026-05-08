package git.wildwind.wwhfas.entity;

import git.wildwind.wwhfas.entity.ai.goal.AmphibiousRandomStrollGoal;
import git.wildwind.wwhfas.entity.ai.goal.CrabAttackGoal;
import git.wildwind.wwhfas.registry.ModEntities;
import git.wildwind.wwhfas.registry.ModItems;
import git.wildwind.wwhfas.tag.ModBlockTags;
import git.wildwind.wwhfas.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.IntFunction;

public class Crab extends Animal implements Bucketable, VariantHolder<Crab.CrabVariant>, GeoEntity {
    public static final SpawnPlacementType SPAWN_PLACEMENT = new SpawnPlacementType() {

        @Override
        public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
            return ModSpawnPlacementTypes.IN_WATER_GROUND.isSpawnPositionOk(level, pos, entityType)
                    || SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(level, pos, entityType);
        }

        @Override
        public BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
            return level.getFluidState(pos).is(Tags.Fluids.WATER) ? pos : SpawnPlacementTypes.ON_GROUND.adjustSpawnPosition(level, pos);
        }
    };

    protected static final RawAnimation HURT_ANIM = RawAnimation.begin().thenPlay("misc.hurt");
    protected static final int CLIENT_SIDE_TURN_TIME = 5;
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);
    private static final int TOTAL_AIR_SUPPLY = 7200;
    private static final int REHYDRATE_AIR_SUPPLY = 1800;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private final PathNavigation waterNavigation;
    private final PathNavigation groundNavigation;
    private int preparingToAttack = -1;
    public float clientSideYRotOffset = 90.0f;
    public float clientSidePreYRotOffset = this.clientSideYRotOffset;

    public Crab(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);

        this.waterNavigation = new AmphibiousPathNavigation(this, level);
        this.groundNavigation = this.navigation;
        this.moveControl = new CrabMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25f));
        this.goalSelector.addGoal(2, new CrabAttackGoal(this, 1.2f, true));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1f, stack -> stack.is(ModItemTags.CRAB_FOOD), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1f));
        this.goalSelector.addGoal(6, new CrabFloatGoal(this, 0.04f));
        this.goalSelector.addGoal(7, new AmphibiousRandomStrollGoal(this, 1.0f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 7.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CaveSpider.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Silverfish.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, CrabVariant.COLD.id);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (level.getFluidState(pos).is(Fluids.WATER)) return 10.0f;
        if (level.getBlockState(pos.below()).is(ModBlockTags.CARB_SPAWNABLE_IN_WATER_GROUND)) return 5.0f;

        return level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public boolean isPreparingToAttack() {
        return preparingToAttack != -1;
    }

    public int getPreparationToAttackDuration() {
        return 12;
    }

    protected void updateClientSideYRotOffset() {
        this.clientSidePreYRotOffset = this.clientSideYRotOffset;

        if (this.shouldTurnBody()) {
            if (this.clientSideYRotOffset < 90.0f) {
                this.clientSideYRotOffset += 90.0f / CLIENT_SIDE_TURN_TIME;
            }
        } else if (this.clientSideYRotOffset > 0.0) {
            this.clientSideYRotOffset -= 90.0f / CLIENT_SIDE_TURN_TIME;
        } else {
            this.clientSideYRotOffset = 0.0f;
        }
    }

    protected boolean shouldTurnBody() {
        Vec3 movement = this.getDeltaMovement();
        return this.onGround()
                && !this.isVisuallySwimming()
                && !this.isImmobile()
                && this.walkAnimation.speed() != 0.0f
                && Math.abs(movement.x) + Math.abs(movement.z) / 2 > 0.015f;
    }

    public boolean prepareAttack(LivingEntity entity) {
        this.setTarget(entity);
        if (entity == null || !entity.isAlive()) return false;

        this.preparingToAttack = getPreparationToAttackDuration();
        this.triggerAnim("Attack", "attack");
        return true;
    }

    @Override
    public void setJumping(boolean jumping) {
    }

    public static boolean checkCrabInWaterGroundSpawnRules(
            EntityType<? extends Crab> crab, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        if (!(MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

        return level.getBlockState(pos.below()).is(ModBlockTags.CARB_SPAWNABLE_IN_WATER_GROUND);
    }

    public static boolean checkCrabOnGroundSpawnRules(EntityType<? extends Crab> crab, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (!(MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

        return level.getBlockState(pos.below()).is(ModBlockTags.CRAB_SPAWNABLE_ON);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.22d)
                .add(Attributes.ATTACK_DAMAGE, 10.0d)
                .add(Attributes.STEP_HEIGHT, 1.0d);
    }

    @Override
    public void baseTick() {
        int currentAirSupply = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) this.handleAirSupply(currentAirSupply);
    }

    @Override
    public int getMaxAirSupply() {
        return TOTAL_AIR_SUPPLY;
    }

    public void handleAirSupply(int currentAirSupply) {
        if (this.isAlive() && !this.isInWaterRainOrBubble()) {
            this.setAirSupply(currentAirSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }

            return;
        }

        this.setAirSupply(this.getMaxAirSupply());
    }

    public void rehydrate() {
        this.setAirSupply(Math.min(this.getAirSupply() + REHYDRATE_AIR_SUPPLY, this.getMaxAirSupply()));
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater()) {
                this.setSwimming(true);
            } else {
                this.setSwimming(false);
            }
        }
    }

    @Override
    public void setSwimming(boolean swimming) {
        super.setSwimming(swimming);

        this.navigation = swimming ? this.waterNavigation : this.groundNavigation;
    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    // TODO: 当前变种生成异常，暂时保持原样，收尾阶段数据驱动化变种后再做调整。
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> biome = level.getBiome(this.blockPosition());
        if (biome.is(Tags.Biomes.IS_COLD_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.COLD.id);
        }else if (biome.is(Tags.Biomes.IS_TEMPERATE_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.TEMPERATE.id);
        }else  if (biome.is(Tags.Biomes.IS_HOT_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.WARM.id);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isPreparingToAttack();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItemTags.CRAB_FOOD);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
            tag.putInt("Variant", this.entityData.get(VARIANT_ID));
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        this.setVariant(CrabVariant.byId(tag.getInt("Variant")));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.CRAB_BUCKET);
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    @Override
    public int getMaxHeadYRot() {
        return 0;
    }

    // TODO: 临时音效，添加专属音效
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL;
    }

    // TODO: 临时音效，添加专属音效
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    // TODO: 临时音效，添加专属音效
    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    // TODO: 临时音效，添加专属音效
    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return super.getHurtSound(damageSource);
    }

    // TODO: 临时音效，添加专属音效
    @Override
    protected SoundEvent getSwimSplashSound() {
        return super.getSwimSplashSound();
    }

    // TODO: 临时音效，添加专属音效
    @Override
    protected SoundEvent getSwimSound() {
        return super.getSwimSound();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.CRAB.get().create(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.entityData.get(VARIANT_ID));
        tag.putBoolean("FromBucket", this.entityData.get(FROM_BUCKET));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(VARIANT_ID, tag.getInt("Variant"));
        this.setFromBucket(tag.getBoolean("FromBucket"));
    }

    @Override
    public void setVariant(CrabVariant variant) {
        this.entityData.set(VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull CrabVariant getVariant() {
        int variantIndex = this.entityData.get(VARIANT_ID);
        if (variantIndex < 0 || variantIndex >= CrabVariant.values().length) {
            return CrabVariant.TEMPERATE;
        }
        return CrabVariant.values()[variantIndex];
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Move", this::moveAnimController));
        controllers.add(new AnimationController<>(this, "Hurt", this::hurtAnimController));
        controllers.add(new AnimationController<>(this, "Attack", state -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING)
        );
    }

    protected PlayState moveAnimController(final AnimationState<Crab> state) {
        return this.isInWater()
                ? state.setAndContinue(DefaultAnimations.SWIM)
                : state.isMoving()
                  ? state.setAndContinue(DefaultAnimations.WALK)
                  : state.setAndContinue(DefaultAnimations.IDLE);
    }

    protected PlayState hurtAnimController(final AnimationState<Crab> state) {
        if (this.isAlive() && this.hurtTime > 0) {
            return state.setAndContinue(HURT_ANIM);
        }

        state.resetCurrentAnimation();
        return PlayState.STOP;
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.isAlive()) return;

        if (this.level().isClientSide()) {
            this.updateClientSideYRotOffset();
        }

        if (this.preparingToAttack > 0) {
            this.preparingToAttack--;
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                this.lookAt(target, 15.0f, 15.0f);
            }

            if (this.preparingToAttack <= 0) {
                this.preparingToAttack = -1;

                if (target != null && target.isAlive() && this.isWithinMeleeAttackRange(target) && this.getSensing().hasLineOfSight(target)) {
                    this.doHurtTarget(target);
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected static class CrabFloatGoal extends Goal {
        private final Mob mob;
        private final float strength;

        public CrabFloatGoal(Mob mob, float strength) {
            this.mob = mob;
            this.strength = strength;
        }

        @Override
        public boolean canUse() {
            MoveControl moveControl = this.mob.getMoveControl();
            return this.mob.isInFluidType((fluidType, height) -> height > this.mob.getBbHeight() && this.mob.canSwimInFluidType(fluidType))
                    && !this.mob.level().getFluidState(new BlockPos((int) moveControl.getWantedX(), (int) moveControl.getWantedY(), (int) moveControl.getWantedZ())).isEmpty();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return false;
        }

        @Override
        public void tick() {
            if (this.mob.getRandom().nextFloat() < 0.8f) {
                this.mob.addDeltaMovement(new Vec3(0.0, this.strength, 0.0));
            }
        }
    }

    protected static class CrabMoveControl extends MoveControl {
        public CrabMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (!mob.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
                super.tick();
                return;
            }

            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                float modifiedSpeed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), modifiedSpeed));
                double relativeX = this.wantedX - this.mob.getX();
                double relativeY = this.wantedY - this.mob.getY();
                double relativeZ = this.wantedZ - this.mob.getZ();
                if (relativeY != 0.0) {
                    double distance = Math.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement()
                            .add(0.0, (double) this.mob.getSpeed() * (relativeY / distance) * 0.1, 0.0)
                    );
                }

                if (relativeX != 0.0 || relativeZ != 0.0) {
                    float yRot = (float) (Mth.atan2(relativeZ, relativeX) * 180.0F / (float) Math.PI) - 90.0F;
                    this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 90.0F));
                    this.mob.yBodyRot = this.mob.getYRot();
                }
            }
        }
    }

    // TODO: 数据驱动
    public enum CrabVariant implements StringRepresentable {
        /**
         * 温 0
         * 热 1
         * 寒 2
         */
        TEMPERATE( 0),
        WARM(2),
        COLD(1);

        private static final IntFunction<CrabVariant> BY_ID = ByIdMap.continuous(CrabVariant::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        private final int id;

        CrabVariant(int id) {
            this.id = id;
        }

        public static CrabVariant byId(int id) {
            return BY_ID.apply(id);
        }

        public int id() {
            return id;
        }

        @Override
        public String getSerializedName() {
            return String.valueOf(this.id);
        }
    }
}
