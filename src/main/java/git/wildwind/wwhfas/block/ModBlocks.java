package git.wildwind.wwhfas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import git.wildwind.wwhfas.WildWindMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, WildWindMod.MOD_ID);

    public static final WoodSet CINDER = registerWoodSet(
        "cinder",
        MapColor.PODZOL,
        MapColor.COLOR_BROWN,
        MapColor.COLOR_BROWN,
        ModTreeGrower.CINDER,
        ModBlockSetType.CINDER,
        ModWoodType.CINDER
    );
    public static final WoodSet EMBER = registerWoodSet(
        "ember",
        MapColor.COLOR_ORANGE,
        MapColor.TERRACOTTA_ORANGE,
        MapColor.COLOR_ORANGE,
        ModTreeGrower.EMBER,
        ModBlockSetType.EMBER,
        ModWoodType.EMBER
    );

    public static final List<WoodSet> WOOD_SETS = List.of(CINDER, EMBER);

    private ModBlocks() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private static WoodSet registerWoodSet(
        String name,
        MapColor barkColor,
        MapColor woodColor,
        MapColor plankColor,
        net.minecraft.world.level.block.grower.TreeGrower treeGrower,
        BlockSetType blockSetType,
        WoodType woodType
    ) {
    DeferredHolder<Block, Block> log = BLOCKS.register(name + "_log", () -> log(MapColor.WOOD, barkColor));
    DeferredHolder<Block, Block> wood = BLOCKS.register(name + "_wood", () -> log(barkColor, barkColor));
    DeferredHolder<Block, Block> strippedLog = BLOCKS.register(
        "stripped_" + name + "_log",
        () -> log(MapColor.WOOD, woodColor)
    );
    DeferredHolder<Block, Block> strippedWood = BLOCKS.register(
        "stripped_" + name + "_wood",
        () -> log(woodColor, woodColor)
    );
    DeferredHolder<Block, Block> leaves = BLOCKS.register(name + "_leaves", () -> leaves());
    DeferredHolder<Block, Block> planks = BLOCKS.register(name + "_planks", () -> planks(plankColor));
    DeferredHolder<Block, Block> stairs = BLOCKS.register(name + "_stairs", () -> legacyStair(planks.get()));
    DeferredHolder<Block, Block> slab = BLOCKS.register(name + "_slab", () -> slab(plankColor));
    DeferredHolder<Block, Block> fence = BLOCKS.register(name + "_fence", () -> fence(plankColor));
    DeferredHolder<Block, Block> fenceGate = BLOCKS.register(name + "_fence_gate", () -> fenceGate(woodType, plankColor));
    DeferredHolder<Block, Block> door = BLOCKS.register(name + "_door", () -> door(blockSetType, plankColor));
    DeferredHolder<Block, Block> trapdoor = BLOCKS.register(name + "_trapdoor", () -> trapdoor(blockSetType, plankColor));
    DeferredHolder<Block, Block> pressurePlate = BLOCKS.register(
        name + "_pressure_plate",
        () -> pressurePlate(blockSetType, plankColor)
    );
    DeferredHolder<Block, Block> button = BLOCKS.register(name + "_button", () -> woodenButton(blockSetType));
    DeferredHolder<Block, Block> sapling = BLOCKS.register(name + "_sapling", () -> sapling(treeGrower));
    DeferredHolder<Block, Block> pottedSapling = BLOCKS.register(
        "potted_" + name + "_sapling",
        () -> pottedSapling(sapling)
    );
    DeferredHolder<Block, Block> sign = BLOCKS.register(name + "_sign", () -> sign(woodType, plankColor));
    DeferredHolder<Block, Block> wallSign = BLOCKS.register(name + "_wall_sign", () -> wallSign(woodType, plankColor, sign));
    DeferredHolder<Block, Block> hangingSign = BLOCKS.register(
        name + "_hanging_sign",
        () -> hangingSign(woodType, plankColor)
    );
    DeferredHolder<Block, Block> wallHangingSign = BLOCKS.register(
        name + "_wall_hanging_sign",
        () -> wallHangingSign(woodType, plankColor, hangingSign)
    );

    return new WoodSet(
        name,
        log,
        wood,
        strippedLog,
        strippedWood,
        leaves,
        planks,
        stairs,
        slab,
        fence,
        fenceGate,
        door,
        trapdoor,
        pressurePlate,
        button,
        sapling,
        pottedSapling,
        sign,
        wallSign,
        hangingSign,
        wallHangingSign
    );
    }

    private static Block log(MapColor topMapColor, MapColor sideMapColor) {
        return new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }

    private static Block leaves() {
        return new LeavesBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isValidSpawn(Blocks::ocelotOrParrot)
                        .isSuffocating(ModBlocks::never)
                        .isViewBlocking(ModBlocks::never)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(ModBlocks::never)
        );
    }

    private static Block planks(MapColor mapColor) {
        return new Block(baseWoodProperties(mapColor));
    }

    private static Block legacyStair(Block baseBlock) {
        return new StairBlock(baseBlock.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(baseBlock));
    }

    private static Block slab(MapColor mapColor) {
        return new SlabBlock(baseWoodProperties(mapColor));
    }

    private static Block fence(MapColor mapColor) {
        return new FenceBlock(baseWoodProperties(mapColor));
    }

    private static Block fenceGate(WoodType woodType, MapColor mapColor) {
        return new FenceGateBlock(woodType, baseWoodProperties(mapColor));
    }

    private static Block door(BlockSetType blockSetType, MapColor mapColor) {
        return new DoorBlock(
                blockSetType,
                BlockBehaviour.Properties.of()
                        .mapColor(mapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(3.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block trapdoor(BlockSetType blockSetType, MapColor mapColor) {
        return new TrapDoorBlock(blockSetType, baseWoodProperties(mapColor).noOcclusion());
    }

    private static Block pressurePlate(BlockSetType blockSetType, MapColor mapColor) {
        return new PressurePlateBlock(blockSetType, baseWoodProperties(mapColor));
    }

    private static Block woodenButton(BlockSetType type) {
        return new ButtonBlock(
                type,
                30,
                BlockBehaviour.Properties.of()
                        .noCollission()
                        .strength(0.5F)
                        .sound(SoundType.WOOD)
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block sapling(net.minecraft.world.level.block.grower.TreeGrower treeGrower) {
        return new SaplingBlock(
                treeGrower,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block pottedSapling(DeferredHolder<Block, Block> sapling) {
        return new FlowerPotBlock(
                () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                sapling,
                BlockBehaviour.Properties.of()
                        .instabreak()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block sign(WoodType woodType, MapColor mapColor) {
        return new StandingSignBlock(woodType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SIGN).mapColor(mapColor));
    }

    private static Block wallSign(WoodType woodType, MapColor mapColor, DeferredHolder<Block, Block> sign) {
        return new WallSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_SIGN).mapColor(mapColor).lootFrom(sign)
        );
    }

    private static Block hangingSign(WoodType woodType, MapColor mapColor) {
        return new CeilingHangingSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN).mapColor(mapColor)
        );
    }

    private static Block wallHangingSign(
            WoodType woodType,
            MapColor mapColor,
            DeferredHolder<Block, Block> hangingSign
    ) {
        return new WallHangingSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(mapColor).lootFrom(hangingSign)
        );
    }

    private static BlockBehaviour.Properties baseWoodProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public record WoodSet(
            String name,
            DeferredHolder<Block, Block> log,
            DeferredHolder<Block, Block> wood,
            DeferredHolder<Block, Block> strippedLog,
            DeferredHolder<Block, Block> strippedWood,
            DeferredHolder<Block, Block> leaves,
            DeferredHolder<Block, Block> planks,
            DeferredHolder<Block, Block> stairs,
            DeferredHolder<Block, Block> slab,
            DeferredHolder<Block, Block> fence,
            DeferredHolder<Block, Block> fenceGate,
            DeferredHolder<Block, Block> door,
            DeferredHolder<Block, Block> trapdoor,
            DeferredHolder<Block, Block> pressurePlate,
            DeferredHolder<Block, Block> button,
            DeferredHolder<Block, Block> sapling,
            DeferredHolder<Block, Block> pottedSapling,
            DeferredHolder<Block, Block> sign,
            DeferredHolder<Block, Block> wallSign,
            DeferredHolder<Block, Block> hangingSign,
            DeferredHolder<Block, Block> wallHangingSign
    ) {
    }
}
