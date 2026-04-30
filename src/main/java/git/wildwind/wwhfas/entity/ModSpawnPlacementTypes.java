package git.wildwind.wwhfas.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface ModSpawnPlacementTypes {
    SpawnPlacementType IN_WATER_GROUND = new SpawnPlacementType() {
        @Override
        public boolean isSpawnPositionOk(@NotNull LevelReader level, @NotNull BlockPos pos, @Nullable EntityType<?> entityType) {
            if (entityType == null) return false;
            if (!level.getWorldBorder().isWithinBounds(pos)) return false;
            BlockPos above = pos.above();
            BlockPos below = pos.below();
            BlockState blockstate = level.getBlockState(below);

            return blockstate.isValidSpawn(level, below, entityType)
                    && this.isValidEmptySpawnBlock(level, pos, entityType)
                    && this.isValidEmptySpawnBlock(level, above, entityType);
        }

        private boolean isValidEmptySpawnBlock(LevelReader level, BlockPos pos, EntityType<?> entityType) {
            BlockState blockState = level.getBlockState(pos);
            return !blockState.isSignalSource()
                    && blockState.getFluidState().is(Fluids.WATER)
                    && !blockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                    && !entityType.isBlockDangerous(blockState);
        }
    };
}
