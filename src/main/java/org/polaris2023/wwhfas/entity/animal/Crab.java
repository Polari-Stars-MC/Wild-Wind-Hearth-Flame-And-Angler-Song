package org.polaris2023.wwhfas.entity.animal;

import com.mojang.logging.LogUtils;
import org.polaris2023.wwhfas.entity.ModSpawnPlacementTypes;
import org.polaris2023.wwhfas.registry.*;
import org.polaris2023.wwhfas.tag.ModBiomeTags;
import org.polaris2023.wwhfas.tag.ModBlockTags;
import org.polaris2023.wwhfas.tag.ModItemTags;
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
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

/**
 * 螃蟹生物实体喵~
 * <br/>
 * 负责陆水两栖移动、变种同步、桶装存取与动画控制喵~
 */
public class Crab extends Animal implements Bucketable, VariantHolder<Holder<CrabVariant>>, GeoEntity {
	/**
	 * 螃蟹使用的自定义生成位置类型喵~
	 */
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
	/**
	 * 客户端渲染时模型的 Y 轴附加旋转偏移喵~
	 */
	public float clientSideModelYRotOffset;
	/**
	 * 客户端渲染时上一刻模型的 Y 轴附加旋转偏移喵~
	 */
	public float clientSidePreModelYRotOffset = this.clientSideModelYRotOffset;
	/**
	 * 客户端渲染时模型的 X 轴附加旋转偏移喵~
	 */
	public float clientSideModelXRotOffset;
	/**
	 * 客户端渲染时上一刻模型的 X 轴附加旋转偏移喵~
	 */
	public float clientSidePreModelXRotOffset = this.clientSideModelXRotOffset;
	private int preparingToAttack = -1;
	private int greetingTicks = -1;

	/**
	 * 创建螃蟹实体喵~
	 *
	 * @param entityType 实体类型喵~
	 * @param level 所在世界喵~
	 */
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
	protected PathNavigation createNavigation(Level level) {
		return new CrabPathNavigation(this, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT_ID, ModCrabVariants.TEMPERATE);
		builder.define(FROM_BUCKET, false);
		builder.define(CLIMBING, false);
	}

	/**
	 * 计算指定位置对螃蟹寻路的吸引值喵~
	 *
	 * @param pos 待评估的位置喵~
	 * @param level 当前关卡读取器喵~
	 * @return 越高表示越适合作为行走目标喵~
	 */
	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		BlockState state = level.getBlockState(pos.below());
		if (state.is(ModBlockTags.CRAB_PREFERRED_WANDER_BLOCKS)) return 10.0f;
		if (state.is(ModBlockTags.CARB_SPAWNABLE_IN_WATER_GROUND)) return 5.0f;

		return level.getPathfindingCostFromLightLevels(pos);
	}

	/**
	 * 判断当前实体在所在位置是否被阻挡喵~
	 *
	 * @param level 当前关卡读取器喵~
	 * @return 未被阻挡时返回 true 喵~
	 */
	@Override
	public boolean checkSpawnObstruction(LevelReader level) {
		return level.isUnobstructed(this);
	}

	/**
	 * 判断当前是否处于攻击蓄力阶段喵~
	 *
	 * @return 处于攻击蓄力时返回 true 喵~
	 */
	public boolean isPreparingToAttack() {
		return this.preparingToAttack != -1;
	}

	/**
	 * 判断当前是否处于打招呼动画阶段喵~
	 *
	 * @return 正在打招呼时返回 true 喵~
	 */
	public boolean isGreeting() {
		return this.greetingTicks != -1;
	}

	/**
	 * 获取攻击蓄力持续刻数喵~
	 *
	 * @return 蓄力持续刻数喵~
	 */
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

	/**
	 * 进入攻击蓄力状态并准备对目标发动攻击喵~
	 *
	 * @param entity 攻击目标喵~
	 * @return 成功进入蓄力状态时返回 true 喵~
	 */
	public boolean prepareAttack(LivingEntity entity) {
		this.setTarget(entity);
		if (entity == null || !entity.isAlive()) return false;

		this.preparingToAttack = getPreparationToAttackDuration();
		this.triggerAnim("Attack", "attack");
		return true;
	}

	/**
	 * 检查螃蟹是否可在水中地面生成喵~
	 *
	 * @param crab 螃蟹实体类型喵~
	 * @param level 世界访问器喵~
	 * @param spawnType 生成类型喵~
	 * @param pos 生成位置喵~
	 * @param random 随机源喵~
	 * @return 满足生成条件时返回 true 喵~
	 */
	public static boolean checkCrabInWaterGroundSpawnRules(
			EntityType<? extends Crab> crab, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
	) {
		if (!(MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

		return level.getBlockState(pos.below()).is(ModBlockTags.CARB_SPAWNABLE_IN_WATER_GROUND);
	}

	/**
	 * 检查螃蟹是否可在陆地生成喵~
	 *
	 * @param crab 螃蟹实体类型喵~
	 * @param level 世界访问器喵~
	 * @param spawnType 生成类型喵~
	 * @param pos 生成位置喵~
	 * @param random 随机源喵~
	 * @return 满足生成条件时返回 true 喵~
	 */
	public static boolean checkCrabOnGroundSpawnRules(EntityType<? extends Crab> crab, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (!(MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

		return level.getBlockState(pos.below()).is(ModBlockTags.CRAB_SPAWNABLE_ON);
	}

	/**
	 * 创建螃蟹实体的基础属性喵~
	 *
	 * @return 属性构建器喵~
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0d)
				.add(Attributes.MOVEMENT_SPEED, 0.22d)
				.add(Attributes.ATTACK_DAMAGE, 10.0d)
				.add(Attributes.STEP_HEIGHT, 1.0d);
	}

	/**
	 * 执行每刻更新并维持空气值变化喵~
	 */
	@Override
	public void baseTick() {
		int currentAirSupply = this.getAirSupply();
		super.baseTick();
		if (!this.isNoAi()) this.handleAirSupply(currentAirSupply);
	}

	/**
	 * 获取螃蟹最大空气值喵~
	 *
	 * @return 最大空气值喵~
	 */
	@Override
	public int getMaxAirSupply() {
		return TOTAL_AIR_SUPPLY;
	}

	/**
	 * 处理离水状态下的空气值变化喵~
	 *
	 * @param currentAirSupply 当前空气值喵~
	 */
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

	/**
	 * 让螃蟹在接触到水后恢复部分空气值喵~
	 */
	public void rehydrate() {
		this.setAirSupply(Math.min(this.getAirSupply() + REHYDRATE_AIR_SUPPLY, this.getMaxAirSupply()));
	}

	/**
	 * 判断当前是否需要主动寻找水源喵~
	 *
	 * @return 需要找水时返回 true 喵~
	 */
	public boolean needWater() {
		return this.getAirSupply() < START_FIND_WATER_AIR_SUPPLY;
	}

	/**
	 * 获取水中移动减速系数喵~
	 *
	 * @return 水中减速系数喵~
	 */
	@Override
	protected float getWaterSlowDown() {
		return 0.98f;
	}

	/**
	 * 更新游泳状态喵~
	 */
	@Override
	public void updateSwimming() {
		if (!this.level().isClientSide) {
			this.setSwimming(this.isEffectiveAi() && this.isInWater());
		}
	}

	/**
	 * 判断是否在视觉上表现为游泳喵~
	 *
	 * @return 是否视觉游泳喵~
	 */
	@Override
	public boolean isVisuallySwimming() {
		return this.isSwimming();
	}

	/**
	 * 判断是否会被流体推动喵~
	 *
	 * @param type 流体类型喵~
	 * @return 不会被推动喵~
	 */
	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	/**
	 * 判断是否需要自定义持久化喵~
	 *
	 * @return 需要时返回 true 喵~
	 */
	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	/**
	 * 判断离玩家较远时是否应被清除喵~
	 *
	 * @param distanceToClosestPlayer 最近玩家距离喵~
	 * @return 不应远距离消失时返回 true 喵~
	 */
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	/**
	 * 处理玩家与螃蟹的交互喵~
	 *
	 * @param player 玩家喵~
	 * @param hand 使用的手喵~
	 * @return 交互结果喵~
	 */
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
	}

	/**
	 * 完成螃蟹生成时的初始化喵~
	 *
	 * @param level 服务端世界喵~
	 * @param difficulty 生成难度喵~
	 * @param spawnType 生成类型喵~
	 * @param spawnGroupData 群组生成数据喵~
	 * @return 生成后的群组数据喵~
	 */
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		if (spawnType == MobSpawnType.BUCKET) return spawnGroupData;

		this.setVariant(getVariantByBiome(level.getBiome(this.blockPosition())));
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	/**
	 * 根据生物群系获取默认螃蟹变种喵~
	 *
	 * @param biome 生物群系喵~
	 * @return 对应变种喵~
	 */
	protected static Holder<CrabVariant> getVariantByBiome(Holder<Biome> biome) {
		if (biome.is(ModBiomeTags.SPAWNS_WARM_VARIANT_CRABS)) return ModCrabVariants.WARM;
		if (biome.is(ModBiomeTags.SPAWNS_COLD_VARIANT_CRABS)) return ModCrabVariants.COLD;

		return ModCrabVariants.TEMPERATE;
	}

	/**
	 * 判断螃蟹是否无视默认行动限制喵~
	 *
	 * @return 处于攻击或打招呼时返回 true 喵~
	 */
	@Override
	protected boolean isImmobile() {
		return super.isImmobile() || this.isPreparingToAttack() || this.isGreeting();
	}

	/**
	 * 判断指定物品是否可作为螃蟹食物喵~
	 *
	 * @param stack 待检查物品喵~
	 * @return 可食用时返回 true 喵~
	 */
	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ModItemTags.CRAB_FOOD);
	}

	/**
	 * 判断该螃蟹是否来自桶装释放喵~
	 *
	 * @return 来自桶装时返回 true 喵~
	 */
	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	/**
	 * 设置该螃蟹是否来自桶装释放喵~
	 *
	 * @param fromBucket 是否来自桶装喵~
	 */
	@Override
	public void setFromBucket(boolean fromBucket) {
		this.entityData.set(FROM_BUCKET, fromBucket);
	}

	/**
	 * 将螃蟹数据写入桶物品标签喵~
	 *
	 * @param stack 蟹桶物品栈喵~
	 */
	@Override
	public void saveToBucketTag(ItemStack stack) {
		Bucketable.saveDefaultDataToBucketTag(this, stack);
		CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag ->
				tag.put("Variant", CrabVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant().value()).getOrThrow()));
	}

	/**
	 * 从桶物品标签中读取螃蟹数据喵~
	 *
	 * @param tag 桶数据标签喵~
	 */
	@Override
	public void loadFromBucketTag(CompoundTag tag) {
		if (tag.contains("Variant")) {
			CrabVariant.CODEC
					.parse(NbtOps.INSTANCE, tag.get("Variant"))
					.resultOrPartial(LOGGER::error)
					.ifPresent(variant -> this.setVariant(ModRegistries.CRAB_VARIANTS.wrapAsHolder(variant)));
		}
	}

	/**
	 * 获取装载该实体所对应的桶物品喵~
	 *
	 * @return 蟹桶物品栈喵~
	 */
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ModItems.CRAB_BUCKET);
	}

	/**
	 * 获取头部 X 轴旋转上限喵~
	 *
	 * @return 头部 X 轴旋转上限喵~
	 */
	@Override
	public int getMaxHeadXRot() {
		return 0;
	}

	/**
	 * 获取头部 Y 轴旋转上限喵~
	 *
	 * @return 头部 Y 轴旋转上限喵~
	 */
	@Override
	public int getMaxHeadYRot() {
		return 0;
	}

	/**
	 * 获取被装桶时播放的音效喵~
	 *
	 * @return 装桶音效喵~
	 */
	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL;
	}

	@Override @Nullable
	protected SoundEvent getAmbientSound() {
		return super.getAmbientSound();
	}

	@Override @Nullable
	protected SoundEvent getDeathSound() {
		return super.getDeathSound();
	}

	@Override @Nullable
	protected SoundEvent getHurtSound(DamageSource damageSource) {
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

	/**
	 * 生成繁殖后的幼年螃蟹实体喵~
	 *
	 * @param level 服务端世界喵~
	 * @param otherParent 另一只亲代喵~
	 * @return 新生成的幼年螃蟹，无法生成时返回 null 喵~
	 */
	@Override @Nullable
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
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

	/**
	 * 保存额外实体数据喵~
	 *
	 * @param tag 保存目标标签喵~
	 */
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("FromBucket", this.entityData.get(FROM_BUCKET));
		tag.put("Variant", CrabVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant().value()).getOrThrow());
	}

	/**
	 * 读取额外实体数据喵~
	 *
	 * @param tag 源数据标签喵~
	 */
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

	/**
	 * 设置螃蟹的变种喵~
	 *
	 * @param variant 变种喵~
	 */
	@Override
	public void setVariant(Holder<CrabVariant> variant) {
		this.entityData.set(VARIANT_ID, variant);
	}

	/**
	 * 获取螃蟹当前的变种喵~
	 *
	 * @return 当前变种喵~
	 */
	@Override
	public Holder<CrabVariant> getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	/**
	 * 注册螃蟹的动画控制器喵~
	 *
	 * @param controllers 动画控制器注册器喵~
	 */
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

	/**
	 * 设置螃蟹当前是否处于攀爬状态喵~
	 *
	 * @param climbing 是否攀爬喵~
	 */
	public void setClimbing(boolean climbing) {
		this.entityData.set(CLIMBING, climbing);
	}

	/**
	 * 判断螃蟹当前是否处于攀爬状态喵~
	 *
	 * @return 攀爬中返回 true 喵~
	 */
	public boolean isClimbing() {
		return this.entityData.get(CLIMBING);
	}

	/**
	 * 判断当前实体是否可攀附喵~
	 *
	 * @return 攀爬中返回 true 喵~
	 */
	@Override
	public boolean onClimbable() {
		return this.isClimbing();
	}

	/**
	 * 执行螃蟹的 AI 逻辑与客户端视觉更新喵~
	 */
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

	/**
	 * 屏蔽默认跳跃状态设置，避免螃蟹出现不符合预期的跳跃表现喵~
	 *
	 * @param jumping 是否正在跳跃喵~
	 */
	@Override
	public void setJumping(boolean jumping) {
	}

	/**
	 * 获取 GeckoLib 动画缓存喵~
	 *
	 * @return 动画缓存喵~
	 */
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
		protected void checkAndPerformAttack(LivingEntity target) {
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
		@Nullable
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
		@Nullable
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
