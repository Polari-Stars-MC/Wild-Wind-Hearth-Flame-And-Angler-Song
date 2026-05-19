package git.wildwind.wwhfas.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 附着在可燃木头侧面的焦枝方块喵~
 */
public class WallScorchedTwigBlock extends Block {
    /**
     * 墙面焦枝方块的编解码器喵~
     */
    public static final MapCodec<WallScorchedTwigBlock> CODEC = simpleCodec(WallScorchedTwigBlock::new);
    /**
     * 墙面焦枝朝向属性喵~
     */
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
        ImmutableMap.of(
            Direction.NORTH, Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
            Direction.SOUTH, Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
            Direction.WEST, Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
            Direction.EAST, Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)
        )
    );

    /**
     * 创建墙面焦枝方块喵~
     *
     * @param properties 方块属性喵~
     */
    public WallScorchedTwigBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * 获取墙面焦枝方块的编解码器喵~
     *
     * @return 墙面焦枝方块编解码器喵~
     */
    @Override
    public MapCodec<WallScorchedTwigBlock> codec() {
        return CODEC;
    }

    /**
     * 获取对应物品的本地化描述键喵~
     *
     * @return 描述键喵~
     */
    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        BlockState supportState = level.getBlockState(supportPos);
        return supportState.is(BlockTags.LOGS_THAT_BURN) && supportState.isFaceSturdy(level, supportPos, facing);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, currentPos)
            ? Blocks.AIR.defaultBlockState()
            : state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState candidate = state.setValue(FACING, direction.getOpposite());
                if (candidate.canSurvive(level, pos)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
