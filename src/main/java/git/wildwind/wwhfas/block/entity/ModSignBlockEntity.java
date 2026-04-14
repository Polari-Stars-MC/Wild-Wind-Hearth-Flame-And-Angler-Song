package git.wildwind.wwhfas.block.entity;

import git.wildwind.wwhfas.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntity extends SignBlockEntity {
    public ModSignBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SIGN.get(), pos, blockState);
    }
}