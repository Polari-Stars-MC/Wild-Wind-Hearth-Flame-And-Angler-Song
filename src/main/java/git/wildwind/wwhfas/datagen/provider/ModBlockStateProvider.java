package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import git.wildwind.wwhfas.block.WallScorchedTwigBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
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
        var scorchedTwigWall = models().withExistingParent("scorched_twig_wall", mcLoc("block/coral_wall_fan"))
            .texture("fan", modLoc("block/scorched_twig_wall"));
        getVariantBuilder(ModTerrainBlocks.SCORCHED_TWIG_WALL.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(scorchedTwigWall)
            .rotationY(((int) state.getValue(WallScorchedTwigBlock.FACING).toYRot() + 180) % 360)
            .build());

        var tinyCactus = models().withExistingParent("tiny_cactus", mcLoc("block/cube_bottom_top"))
            .texture("bottom", mcLoc("block/cactus_bottom"))
            .texture("top", mcLoc("block/cactus_top"))
            .texture("side", mcLoc("block/cactus_side"))
            .texture("particle", mcLoc("block/cactus_side"));
        simpleBlockWithItem(ModTerrainBlocks.TINY_CACTUS.get(), tinyCactus);

        var fletchiingTable = models().withExistingParent("fletchiing_table", mcLoc("block/cube"))
            .texture("down", mcLoc("block/birch_planks"))
            .texture("up", mcLoc("block/fletching_table_top"))
            .texture("north", mcLoc("block/fletching_table_front"))
            .texture("south", mcLoc("block/fletching_table_front"))
            .texture("east", mcLoc("block/fletching_table_side"))
            .texture("west", mcLoc("block/fletching_table_side"))
            .texture("particle", mcLoc("block/fletching_table_front"));
        simpleBlockWithItem(ModTerrainBlocks.FLETCHIING_TABLE.get(), fletchiingTable);
    }
}
