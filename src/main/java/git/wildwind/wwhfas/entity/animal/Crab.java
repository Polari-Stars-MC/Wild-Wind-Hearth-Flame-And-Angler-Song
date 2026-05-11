package git.wildwind.wwhfas.entity.animal;

import com.mojang.logging.LogUtils;
import git.wildwind.wwhfas.entity.ModSpawnPlacementTypes;
import git.wildwind.wwhfas.registry.*;
import git.wildwind.wwhfas.tag.ModBiomeTags;
import git.wildwind.wwhfas.tag.ModBlockTags;
import git.wildwind.wwhfas.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
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
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class Crab extends Animal implements Bucketable, VariantHolder<Holder<CrabVariant>>, GeoEntity {
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
    protected static final RawAnimation GREETING_ANIM = RawAnimation.begin().thenPlay("misc.greeting");
    protected static final float CLIENT_SIDE_MAX_MODEL_ROT = 90.0f;
    protected static final int CLIENT_SIDE_MODEL_ROT_TIME = 5;
    protected static final float CLIENT_SIDE_MODEL_ROT_PER_TICK = CLIENT_SIDE_MAX_MODEL_ROT / CLIENT_SIDE_MODEL_ROT_TIME;
    private static final EntityDataAccessor<Holder<CrabVariant>> VARIANT_ID = SynchedEntityData.defineId(Crab.class, ModEntityDataSerializers.CRAB_VARIANT.get());
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int TOTAL_AIR_SUPPLY = 7200;
    private static final int START_FIND_WATER_AIR_SUPPLY = 2400;
    private static final int REHYDRATE_AIR_SUPPLY = 1800;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public float clientSideModelYRotOffset;
    public float clientSidePreModelYRotOffset = this.clientSideModelYRotOffset;
    public float clientSideModelXRotOffset;
    public float clientSidePreModelXRotOffset = this.clientSideModelXRotOffset;
    private int preparingToAttack = -1;
    private int greetingTicks = -1;

    public Crab(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.moveControl = new CrabMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25f));
        this.goalSelector.addGoal(2, new CrabFindWaterGoal(this, 1.25f, 12, 80));
        this.goalSelector.addGoal(3, new CrabAttackGoal(this, 1.2f, true));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.1f, stack -> stack.is(ModItemTags.CRAB_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1f));
        this.goalSelector.addGoal(7, new CrabRandomStrollGoal(this, 1.0f));
        this.goalSelector.addGoal(8, new CrabFloatGoal(this, 0.04f));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 7.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CaveSpider.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Silverfish.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level level) {
        return new CrabPathNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, ModCrabVariants.TEMPERATE);
        builder.define(FROM_BUCKET, false);
        builder.define(CLIMBING, false);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        BlockState state = level.getBlockState(pos.below());
        if (state.is(ModBlockTags.CRAB_PREFERRED_WANDER_BLOCKS)) return 10.0f;
        if (state.is(ModBlockTags.CARB_SPAWNABLE_IN_WATER_GROUND)) return 5.0f;

        return level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public boolean isPreparingToAttack() {
        return this.preparingToAttack != -1;
    }

    public boolean isGreeting() {
        return this.greetingTicks != -1;
    }

    public int getPreparationToAttackDuration() {
        return 12;
    }

    protected void updateClientSideVisuals() {
        this.clientSidePreModelYRotOffset = this.clientSideModelYRotOffset;
        this.clientSidePreModelXRotOffset = this.clientSideModelXRotOffset;

        if (this.shouldRotModelY()) {
            if (this.clientSideModelYRotOffset < CLIENT_SIDE_MAX_MODEL_ROT) {
                this.clientSideModelYRotOffset += CLIENT_SIDE_MODEL_ROT_PER_TICK;
            }
        } else if (this.clientSideModelYRotOffset > 0.0 && this.clientSideModelXRotOffset <= 0.0f) {
            this.clientSideModelYRotOffset -= CLIENT_SIDE_MODEL_ROT_PER_TICK;
        }

        if (this.shouldRotModelX()) {
            if (this.clientSideModelXRotOffset < CLIENT_SIDE_MAX_MODEL_ROT) {
                this.clientSideModelXRotOffset += CLIENT_SIDE_MODEL_ROT_PER_TICK;
            }
        } else if (this.clientSideModelXRotOffset > 0.0) {
            this.clientSideModelXRotOffset -= CLIENT_SIDE_MODEL_ROT_PER_TICK;
        }
    }

    protected boolean shouldRotModelY() {
        Vec3 movement = this.getDeltaMovement();
        return ((this.onGround() && Math.abs(movement.x) + Math.abs(movement.z) / 2 > 0.015f && this.walkAnimation.speed() != 0.0f)
                || (!this.onGround() && this.isClimbing() && Math.abs(movement.y) > 0.005f))
                && !this.isVisuallySwimming()
                && !this.isImmobile();
    }

    protected boolean shouldRotModelX() {
        return !this.onGround() && !this.isVisuallySwimming() && this.isClimbing();
    }

    public boolean prepareAttack(LivingEntity entity) {
        this.setTarget(entity);
        if (entity == null || !entity.isAlive()) return false;

        this.preparingToAttack = getPreparationToAttackDuration();
        this.triggerAnim("Attack", "attack");
        return true;
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
        return Mob.createMobAttributes()
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
                this.hurt(this.damageSources().dryOut(), 1.0F);
            }

            return;
        }

        this.setAirSupply(this.getMaxAirSupply());
    }

    public void rehydrate() {
        this.setAirSupply(Math.min(this.getAirSupply() + REHYDRATE_AIR_SUPPLY, this.getMaxAirSupply()));
    }

    public boolean needWater() {
        return this.getAirSupply() < START_FIND_WATER_AIR_SUPPLY;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            this.setSwimming(this.isEffectiveAi() && this.isInWater());
        }
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

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType == MobSpawnType.BUCKET) return spawnGroupData;

        this.setVariant(getVariantByBiome(level.getBiome(this.blockPosition())));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected static Holder<CrabVariant> getVariantByBiome(Holder<Biome> biome) {
        if (biome.is(ModBiomeTags.SPAWNS_WARM_VARIANT_CRABS)) return ModCrabVariants.WARM;
        if (biome.is(ModBiomeTags.SPAWNS_COLD_VARIANT_CRABS)) return ModCrabVariants.COLD;

        return ModCrabVariants.TEMPERATE;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isPreparingToAttack() || this.isGreeting();
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
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag ->
                tag.put("Variant", CrabVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant().value()).getOrThrow()));
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        if (tag.contains("Variant")) {
            CrabVariant.CODEC
                    .parse(NbtOps.INSTANCE, tag.get("Variant"))
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(variant -> this.setVariant(ModRegistries.CRAB_VARIANTS.wrapAsHolder(variant)));
        }
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

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return super.getHurtSound(damageSource);
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return super.getSwimSplashSound();
    }

    @Override
    protected SoundEvent getSwimSound() {
        return super.getSwimSound();
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.spawnAtLocation(ModItems.CRAB_CLAW.get(), 1);
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Crab crab = ModEntities.CRAB.get().create(level);
        if (crab != null) {
            var variantList = SimpleWeightedRandomList.<Holder<CrabVariant>>builder();
            variantList.add(this.getVariant());
            if (otherParent instanceof Crab otherCrab) {
                variantList.add(otherCrab.getVariant());
            }
            variantList.add(getVariantByBiome(level.getBiome(this.blockPosition())));

            Holder<CrabVariant> variant = variantList
                    .build()
                    .getRandomValue(crab.getRandom())
                    .orElse(ModCrabVariants.TEMPERATE);
            crab.setVariant(variant);
        }

        return crab;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.entityData.get(FROM_BUCKET));
        tag.put("Variant", CrabVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant().value()).getOrThrow());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) {
            CrabVariant.CODEC
                    .parse(NbtOps.INSTANCE, tag.get("Variant"))
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(variant -> this.setVariant(ModRegistries.CRAB_VARIANTS.wrapAsHolder(variant)));
        }
        this.setFromBucket(tag.getBoolean("FromBucket"));
    }

    @Override
    public void setVariant(Holder<CrabVariant> variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    public @NotNull Holder<CrabVariant> getVariant() {
        return this.entityData.get(VARIANT_ID);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Move", 3, this::moveAnimController));
        controllers.add(new AnimationController<>(this, "Hurt", this::hurtAnimController));
        controllers.add(new AnimationController<>(this, "Attack", state -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING)
        );
        controllers.add(new AnimationController<>(this, "Greeting", state -> PlayState.STOP)
                .triggerableAnim("greeting", GREETING_ANIM)
        );
    }

    protected PlayState moveAnimController(final AnimationState<Crab> state) {
        return this.isInWater()
                ? state.setAndContinue(DefaultAnimations.SWIM)
                : state.isMoving() || this.isClimbing()
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

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.isAlive()) return;

        if (this.level().isClientSide()) {
            this.updateClientSideVisuals();
            return;
        }

        if (!this.isEffectiveAi()) return;

        this.setClimbing(this.horizontalCollision);

        if (this.preparingToAttack > 0) {
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                this.lookAt(target, 15.0f, 15.0f);
            }

            if (--this.preparingToAttack <= 0) {
                this.preparingToAttack = -1;

                if (target != null && target.isAlive() && this.isWithinMeleeAttackRange(target) && this.getSensing().hasLineOfSight(target)) {
                    this.doHurtTarget(target);
                }
            }
        }

        if (this.greetingTicks > 0) {
            if (--this.greetingTicks <= 0) {
                this.greetingTicks = -1;
            }
        }

        if (!this.isInWater() && !this.isGreeting() && this.tickCount % 2 == 0 && this.random.nextFloat() <= 0.001) {
            AABB box = this.getBoundingBox().inflate(6.0, 2.0, 6.0);
            for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
                if (this.level().getBlockState(pos).is(ModBlockTags.CRAB_PREFERRED_WANDER_BLOCKS)) {
                    this.greetingTicks = 27;
                    this.triggerAnim("Greeting", "greeting");
                }
            }
        }
    }

    @Override
    public void setJumping(boolean jumping) {
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
            this.mob.getNavigation().setCanFloat(true);
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
        private final Crab crab;

        public CrabMoveControl(Crab crab) {
            super(crab);
            this.crab = crab;
        }

        @Override
        public void tick() {
            if (!crab.isInWater()) {
                super.tick();
                return;
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && this.shouldContinueMoveTo()) {
                float modifiedSpeed = (float) (this.speedModifier * this.crab.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.crab.setSpeed(Mth.lerp(0.125F, this.crab.getSpeed(), modifiedSpeed));
                double relativeX = this.wantedX - this.crab.getX();
                double relativeY = this.wantedY - this.crab.getY();
                double relativeZ = this.wantedZ - this.crab.getZ();
                if (relativeY != 0.0) {
                    double distance = Math.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);
                    this.crab.setDeltaMovement(this.crab.getDeltaMovement()
                            .add(0.0, (double) this.crab.getSpeed() * (relativeY / distance) * 0.1, 0.0)
                    );
                }

                if (relativeX != 0.0 || relativeZ != 0.0) {
                    float yRot = (float) (Mth.atan2(relativeZ, relativeX) * 180.0F / (float) Math.PI) - CLIENT_SIDE_MAX_MODEL_ROT;
                    this.crab.setYRot(this.rotlerp(this.crab.getYRot(), yRot, CLIENT_SIDE_MAX_MODEL_ROT));
                    this.crab.yBodyRot = this.crab.getYRot();
                }
            }
        }

        private boolean shouldContinueMoveTo() {
            PathNavigation navigation = this.crab.getNavigation();
            return navigation instanceof CrabPathNavigation crabPathNavigation
                    ? !crabPathNavigation.isDone() || (this.crab.isInWater() && crabPathNavigation.fallbackPos != null)
                    : !navigation.isDone();
        }
    }

    protected static class CrabAttackGoal extends MeleeAttackGoal {
        private final Crab crab;

        public CrabAttackGoal(Crab crab, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(crab, speedModifier, followingTargetEvenIfNotSeen);
            this.crab = crab;
        }

        @Override
        public boolean canUse() {
            return !this.crab.isImmobile() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.crab.isImmobile() && super.canContinueToUse();
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity target) {
            if (!canPerformAttack(target)) return;
            this.resetAttackCooldown();
            this.crab.prepareAttack(target);
        }
    }

    protected static class CrabFindWaterGoal extends Goal {
        private final Crab crab;
        private final float speedModifier;
        private final int waterSearchRange;
        private final int interval;
        private BlockPos waterPos;
        private int cooldown;

        public CrabFindWaterGoal(Crab crab, float speedModifier, int waterSearchRange, int interval) {
            this.crab = crab;
            this.speedModifier = speedModifier;
            this.waterSearchRange = waterSearchRange;
            this.interval = interval;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.crab.needWater()) return false;

            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }

            for (BlockPos pos : BlockPos.betweenClosed(
                    (int) (this.crab.getX() - waterSearchRange),
                    (int) this.crab.getY() - waterSearchRange,
                    (int) this.crab.getZ() - waterSearchRange / 2,
                    (int) this.crab.getX() + waterSearchRange,
                    (int) this.crab.getY() + waterSearchRange / 2,
                    (int) this.crab.getZ() + waterSearchRange
            )) {
                if (this.crab.level().getFluidState(pos).is(FluidTags.WATER)) {
                    this.waterPos = pos;
                    break;
                }
            }
            this.cooldown = this.interval;

            return waterPos != null;
        }

        @Override
        public void start() {
            this.crab.getNavigation().moveTo(
                    this.waterPos.getX(),
                    this.waterPos.getY(),
                    this.waterPos.getZ(),
                    this.speedModifier
            );

            this.waterPos = null;
        }
    }

    protected static class CrabRandomStrollGoal extends RandomStrollGoal {
        public CrabRandomStrollGoal(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        @Override
        protected @Nullable Vec3 getPosition() {
            Level level = this.mob.level();
            Vec3 position = null;

            if (level.isNight() || level.isRaining()) {
                position = LandRandomPos.getPos(this.mob, 20, 4, pos ->
                        level.getBlockState(pos.above()).is(ModBlockTags.CRAB_PREFERRED_WANDER_BLOCKS)
                                ? 10.0
                                : 0.0
                );
            }

            if (position == null) {
                position = DefaultRandomPos.getPos(this.mob, 10, 7);
            }

            return position;
        }
    }

    protected static class CrabPathNavigation extends GroundPathNavigation {
        private final Crab crab;
        private BlockPos fallbackPos;

        public CrabPathNavigation(Crab crab, Level level) {
            super(crab, level);
            this.crab = crab;
        }

        @Override
        protected PathFinder createPathFinder(int maxVisitedNodes) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(true);
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        protected Vec3 getTempMobPos() {
            return new Vec3(this.mob.getX(), this.mob.getY(0.5), this.mob.getZ());
        }

        @Override
        public Path createPath(BlockPos pos, int accuracy) {
            this.fallbackPos = pos;
            return super.createPath(pos, accuracy);
        }

        @Override
        public Path createPath(Entity entity, int accuracy) {
            this.fallbackPos = entity.blockPosition();
            return super.createPath(entity, accuracy);
        }

        @Override
        public boolean moveTo(Entity entity, double speed) {
            Path path = this.createPath(entity, 0);
            if (path != null) {
                return this.moveTo(path, speed);
            } else {
                this.fallbackPos = entity.blockPosition();
                this.speedModifier = speed;
                return true;
            }
        }

        @Override
        public void tick() {
            if (!this.isDone()) {
                super.tick();
                return;
            }

            if (forwardToFallbackPos()) {
                MoveControl moveControl = this.mob.getMoveControl();
                moveControl.setWantedPosition(this.fallbackPos.getX(), this.fallbackPos.getY(), this.fallbackPos.getZ(), this.speedModifier);
                return;
            }

            this.fallbackPos = null;
        }

        private boolean forwardToFallbackPos() {
            if (fallbackPos == null) return false;
            if (this.crab.isClimbing()) return !this.crab.verticalCollision;

            double horizontalDistance = fallbackPos.distToCenterSqr(mob.getX(), fallbackPos.getY(), mob.getZ());
            if (horizontalDistance < Mth.square(Math.max(this.mob.getBbWidth(), 1.0))) {
                BlockPos pos = this.mob.blockPosition();
                BlockState state = this.level.getBlockState(pos);
                if (state.getCollisionShape(level, pos).isEmpty()) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        BlockPos relativePos = pos.relative(direction);
                        if (!level.getBlockState(relativePos).getCollisionShape(level, relativePos).isEmpty()) {
                            return true;
                        }
                    }
                }

                return false;
            }

            return true;
        }

        @Override
        protected double getGroundY(Vec3 vec) {
            return vec.y;
        }

        @Override
        protected boolean canMoveDirectly(Vec3 pos1, Vec3 pos2) {
            return this.mob.isInLiquid() && isClearForMovementBetween(this.mob, pos1, pos2, true);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return !this.level.getBlockState(pos.below()).isAir();
        }
    }
}
