package org.polaris2023.wwhfas.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class AttachParentSpawnEggItem extends DeferredSpawnEggItem {
    private final Set<EntityType<? extends Mob>> parentTypes;

    public AttachParentSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props, EntityType<? extends Mob>... parentTypes) {
        super(type, backgroundColor, highlightColor, props);
        this.parentTypes = new HashSet<>(Set.of(parentTypes));
    }


    @Override
    public boolean spawnsEntity(ItemStack stack, EntityType<?> entityType) {
        return super.spawnsEntity(stack, entityType) || this.parentTypes.contains(entityType);
    }

    @Override
    public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob p_mob, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 pos, ItemStack stack) {
        if (this.parentTypes.contains(entityType)) {
            Mob mob = (Mob) this.getDefaultType().create(serverLevel);
            if (mob != null) {
                mob.moveTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
                serverLevel.addFreshEntityWithPassengers(mob);
                mob.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
                stack.consume(1, player);
                return Optional.of(mob);
            }
        }

        return super.spawnOffspringFromSpawnEgg(player, p_mob, entityType, serverLevel, pos, stack);
    }
}
