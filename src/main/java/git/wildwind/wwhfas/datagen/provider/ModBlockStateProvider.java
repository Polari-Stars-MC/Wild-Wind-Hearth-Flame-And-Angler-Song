package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
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

    simpleBlockWithItem((LeavesBlock) woodSet.leaves().get(), cubeAll(woodSet.leaves().get()));
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
}
