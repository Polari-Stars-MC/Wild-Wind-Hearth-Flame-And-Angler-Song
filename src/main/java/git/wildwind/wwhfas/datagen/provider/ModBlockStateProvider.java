package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import git.wildwind.wwhfas.block.WallScorchedTwigBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WildWindMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
            registerWoodSet(woodSet);
        }
        registerTerrainBlocks();
    }

    private void registerWoodSet(ModBlocks.WoodSet woodSet) {
        String name = woodSet.name();

        axisBlock((RotatedPillarBlock) woodSet.log().get(), modLoc("block/" + name + "_log"), modLoc("block/" + name + "_log_top"));
        axisBlock((RotatedPillarBlock) woodSet.wood().get(), modLoc("block/" + name + "_log"), modLoc("block/" + name + "_log"));
        axisBlock(
            (RotatedPillarBlock) woodSet.strippedLog().get(),
            modLoc("block/stripped_" + name + "_log"),
            modLoc("block/stripped_" + name + "_log_top")
        );
        axisBlock(
            (RotatedPillarBlock) woodSet.strippedWood().get(),
            modLoc("block/stripped_" + name + "_log"),
            modLoc("block/stripped_" + name + "_log")
        );

        simpleBlockWithItem(woodSet.planks().get(), cubeAll(woodSet.planks().get()));
        stairsBlock((StairBlock) woodSet.stairs().get(), blockTexture(woodSet.planks().get()));
        slabBlock((SlabBlock) woodSet.slab().get(), blockTexture(woodSet.planks().get()), blockTexture(woodSet.planks().get()));
        fenceBlock((FenceBlock) woodSet.fence().get(), blockTexture(woodSet.planks().get()));
        fenceGateBlock((FenceGateBlock) woodSet.fenceGate().get(), blockTexture(woodSet.planks().get()));
        doorBlockWithRenderType(
            (DoorBlock) woodSet.door().get(),
            modLoc("block/" + name + "_door_bottom"),
            modLoc("block/" + name + "_door_top"),
            "cutout"
        );
        trapdoorBlockWithRenderType(
            (TrapDoorBlock) woodSet.trapdoor().get(),
            modLoc("block/" + name + "_trapdoor"),
            true,
            "cutout"
        );
        buttonBlock((ButtonBlock) woodSet.button().get(), blockTexture(woodSet.planks().get()));
        pressurePlateBlock((PressurePlateBlock) woodSet.pressurePlate().get(), blockTexture(woodSet.planks().get()));
        signBlock(
            (StandingSignBlock) woodSet.sign().get(),
            (WallSignBlock) woodSet.wallSign().get(),
            blockTexture(woodSet.planks().get())
        );
        hangingSignBlock(
            (CeilingHangingSignBlock) woodSet.hangingSign().get(),
            (WallHangingSignBlock) woodSet.wallHangingSign().get(),
            blockTexture(woodSet.planks().get())
        );

        if (woodSet.hasTreeBlocks()) {
            simpleBlockWithItem((LeavesBlock) woodSet.leaves().get(), cubeAll(woodSet.leaves().get()));
            simpleBlock(
                woodSet.sapling().get(),
                models().cross(name + "_sapling", modLoc("block/" + name + "_sapling")).renderType("cutout")
            );
            simpleBlock(
                woodSet.pottedSapling().get(),
                models().singleTexture(
                    "potted_" + name + "_sapling",
                    mcLoc("block/flower_pot_cross"),
                    "plant",
                    modLoc("block/" + name + "_sapling")
                ).renderType("cutout")
            );
        }

        simpleBlockItem(woodSet.log().get(), models().getExistingFile(modLoc("block/" + name + "_log")));
        simpleBlockItem(woodSet.wood().get(), models().getExistingFile(modLoc("block/" + name + "_wood")));
        simpleBlockItem(woodSet.strippedLog().get(), models().getExistingFile(modLoc("block/stripped_" + name + "_log")));
        simpleBlockItem(woodSet.strippedWood().get(), models().getExistingFile(modLoc("block/stripped_" + name + "_wood")));
        simpleBlockItem(woodSet.stairs().get(), models().getExistingFile(modLoc("block/" + name + "_stairs")));
        simpleBlockItem(woodSet.slab().get(), models().getExistingFile(modLoc("block/" + name + "_slab")));
        simpleBlockItem(woodSet.fenceGate().get(), models().getExistingFile(modLoc("block/" + name + "_fence_gate")));
        simpleBlockItem(woodSet.trapdoor().get(), models().getExistingFile(modLoc("block/" + name + "_trapdoor_bottom")));
        simpleBlockItem(woodSet.pressurePlate().get(), models().getExistingFile(modLoc("block/" + name + "_pressure_plate")));
    }

    private void registerTerrainBlocks() {
        var scorchedGrassBlock = models().withExistingParent("scorched_grass_block", mcLoc("block/cube_bottom_top"))
            .texture("bottom", modLoc("block/scorched_dirt"))
            .texture("top", modLoc("block/scorched_grass_block_top"))
            .texture("side", modLoc("block/scorched_grass_block_side"))
            .texture("particle", modLoc("block/scorched_dirt"));
        var scorchedGrassBlockSnowy = models().withExistingParent("scorched_grass_block_snowy", mcLoc("block/cube_bottom_top"))
            .texture("bottom", modLoc("block/scorched_dirt"))
            .texture("top", modLoc("block/scorched_grass_block_top"))
            .texture("side", mcLoc("block/grass_block_snow"))
            .texture("particle", modLoc("block/scorched_dirt"));

        getVariantBuilder(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(SnowyDirtBlock.SNOWY) ? scorchedGrassBlockSnowy : scorchedGrassBlock)
            .build());
        simpleBlockItem(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get(), scorchedGrassBlock);

        simpleBlockWithItem(ModTerrainBlocks.SCORCHED_DIRT.get(), cubeAll(ModTerrainBlocks.SCORCHED_DIRT.get()));

        simpleBlock(
            ModTerrainBlocks.SCORCHED_GRASS.get(),
            models().cross("scorched_grass", modLoc("block/scorched_grass")).renderType("cutout")
        );
        simpleBlock(
            ModTerrainBlocks.SCORCHED_TWIG.get(),
            models().cross("scorched_twig", modLoc("block/scorched_twig")).renderType("cutout")
        );
        var scorchedTwigWall = wallCrossModel("scorched_twig_wall", modLoc("block/scorched_twig_wall"));
        getVariantBuilder(ModTerrainBlocks.SCORCHED_TWIG_WALL.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(scorchedTwigWall)
            .rotationY(((int) state.getValue(WallScorchedTwigBlock.FACING).toYRot() + 270) % 180)
            .build());

        simpleBlock(
            ModTerrainBlocks.TINY_CACTUS.get(),
            models().cross("tiny_cactus", modLoc("block/tiny_cactus")).renderType("cutout")
        );
    }

    private ModelFile wallCrossModel(String name, net.minecraft.resources.ResourceLocation texture) {
        BlockModelBuilder model = models().getBuilder(name)
            .ao(false)
            .renderType("cutout")
            .texture("particle", texture)
            .texture("cross", texture);

        addCrossElement(model,
                0.0F, 0.0F, 8F,   // from
                16.0F, 16.0F, 8F, // to
                45.0F,              // angle
                8.0F, 8.0F, 8.0F    // origin
        );

        // Element 2: 负 45 度 (注意 Y 轴范围变成了 1~17)
        addCrossElement(model,
                0.0F, 0.0F, 8F,   // from Y=1
                16.0F, 16.0F, 8F, // to Y=17 (超出方块边界)
                -45.0F,             // angle
                8.0F, 8.0F, 8.0F    // origin Y=9
        );
        return model;
    }

    private void addCrossElement(BlockModelBuilder model,
                                 float fromX, float fromY, float fromZ,
                                 float toX, float toY, float toZ,
                                 float angle, float originX, float originY, float originZ) {
        model.element()
                .from(fromX, fromY, fromZ)
                .to(toX, toY, toZ)
                .shade(false)
                .rotation()
                .angle(angle)
                .axis(Direction.Axis.X) // 注意：这里必须改为 X 轴
                .origin(originX, originY, originZ)
                // JSON 中没有 rescale，保持默认即可 (false)
                .end()
                // --- 6 个面的定义，完全照搬 JSON 数据 ---
                .face(Direction.NORTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#cross").end()
                .face(Direction.SOUTH).uvs(16.0F, 0.0F, 0.0F, 16.0F).texture("#cross").end() // JSON 中南面未翻转
                .face(Direction.EAST) .uvs(8.0F, 0.0F, 8.0F, 16.0F).texture("#cross").end()  // 侧面纹理切片
                .face(Direction.WEST) .uvs(0.0F, 8.0F, 16.0F, 8.0F).texture("#cross").end()
                .face(Direction.UP)   .uvs(0.0F, 8.0F, 16.0F, 8.0F).texture("#cross").end()
                .face(Direction.DOWN) .uvs(0.0F, 8.0F, 16.0F, 8.0F).texture("#cross").end()
                .end(); // 结束 Element
    }
}
