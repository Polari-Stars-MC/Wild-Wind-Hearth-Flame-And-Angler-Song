package git.wildwind.wwhfas.entity;

import git.wildwind.wwhfas.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Crab extends Mob implements Bucketable {


    public Crab(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
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
}
