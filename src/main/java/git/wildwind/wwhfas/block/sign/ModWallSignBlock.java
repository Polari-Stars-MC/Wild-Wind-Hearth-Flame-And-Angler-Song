package git.wildwind.wwhfas.block.sign;

import git.wildwind.wwhfas.block.entity.ModSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

public class ModWallSignBlock extends WallSignBlock {
    private final Supplier<BlockEntityType<? extends SignBlockEntity>> blockEntityType;

    public ModWallSignBlock(
            WoodType woodType,
            Properties properties,
            Supplier<BlockEntityType<? extends SignBlockEntity>> blockEntityType
    ) {
        super(woodType, properties);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModSignBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, this.blockEntityType.get(), SignBlockEntity::tick);
    }
}