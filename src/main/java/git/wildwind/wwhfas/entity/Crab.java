package git.wildwind.wwhfas.entity;

import git.wildwind.wwhfas.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class Crab extends Animal implements Bucketable {



    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.INT);

    public Crab(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, CrabVariant.COLD.id);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> biome = level.getBiome(this.blockPosition());
        if (biome.is(Tags.Biomes.IS_COLD_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.COLD.id);
        }else if (biome.is(Tags.Biomes.IS_TEMPERATE_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.TEMPERATE.id);
        }else  if (biome.is(Tags.Biomes.IS_HOT_OVERWORLD)){
            this.entityData.set(VARIANT_ID,CrabVariant.HOT.id);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean fromBucket() {
        return false;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {

    }

    @Override
    public void saveToBucketTag(ItemStack stack) {

    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {

    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.CRAB_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.entityData.get(VARIANT_ID));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(VARIANT_ID,compound.getInt("Variant")
        );
    }

    public CrabVariant getVariant() {
        int variantIndex = this.entityData.get(VARIANT_ID);
        if (variantIndex < 0 || variantIndex >= CrabVariant.values().length) {
            return CrabVariant.TEMPERATE;
        }
        return CrabVariant.values()[variantIndex];
    }

    public enum CrabVariant implements StringRepresentable {
        /**
         * 温 0
         * 寒 1
         * 热 2
         */
        TEMPERATE( 0),
        COLD(1),
        HOT(2);

        private final int id;

        CrabVariant( int id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return String.valueOf(this.id);
        }
    }
}
