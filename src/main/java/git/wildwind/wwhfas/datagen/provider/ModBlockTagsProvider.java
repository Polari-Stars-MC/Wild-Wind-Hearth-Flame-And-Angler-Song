package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, WildWindMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
            TagKey<Block> logsTag = TagKey.create(
                net.minecraft.core.registries.Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, woodSet.name() + "_logs")
            );

            tag(logsTag)
                .add(woodSet.log().get(), woodSet.wood().get(), woodSet.strippedLog().get(), woodSet.strippedWood().get());

            tag(BlockTags.LOGS)
                .addTag(logsTag);
            tag(BlockTags.LOGS_THAT_BURN)
                .addTag(logsTag);
            tag(BlockTags.PLANKS)
                .add(woodSet.planks().get());
            tag(BlockTags.WOODEN_STAIRS)
                .add(woodSet.stairs().get());
            tag(BlockTags.WOODEN_SLABS)
                .add(woodSet.slab().get());
            tag(BlockTags.WOODEN_FENCES)
                .add(woodSet.fence().get());
            tag(BlockTags.FENCE_GATES)
                .add(woodSet.fenceGate().get());
            tag(BlockTags.WOODEN_DOORS)
                .add(woodSet.door().get());
            tag(BlockTags.WOODEN_TRAPDOORS)
                .add(woodSet.trapdoor().get());
            tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(woodSet.pressurePlate().get());
            tag(BlockTags.WOODEN_BUTTONS)
                .add(woodSet.button().get());
            tag(BlockTags.STANDING_SIGNS)
                .add(woodSet.sign().get());
            tag(BlockTags.WALL_SIGNS)
                .add(woodSet.wallSign().get());
            tag(BlockTags.ALL_SIGNS)
                .add(woodSet.sign().get(), woodSet.wallSign().get());
            tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(woodSet.hangingSign().get());
            tag(BlockTags.WALL_HANGING_SIGNS)
                .add(woodSet.wallHangingSign().get());
            tag(BlockTags.ALL_HANGING_SIGNS)
                .add(woodSet.hangingSign().get(), woodSet.wallHangingSign().get());
            tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                    woodSet.log().get(),
                    woodSet.wood().get(),
                    woodSet.strippedLog().get(),
                    woodSet.strippedWood().get(),
                    woodSet.planks().get(),
                    woodSet.stairs().get(),
                    woodSet.slab().get(),
                    woodSet.fence().get(),
                    woodSet.fenceGate().get(),
                    woodSet.door().get(),
                    woodSet.trapdoor().get(),
                    woodSet.pressurePlate().get(),
                    woodSet.button().get(),
                    woodSet.sign().get(),
                    woodSet.wallSign().get(),
                    woodSet.hangingSign().get(),
                    woodSet.wallHangingSign().get()
                );
            tag(Tags.Blocks.STRIPPED_LOGS)
                .add(woodSet.strippedLog().get());
            tag(Tags.Blocks.STRIPPED_WOODS)
                .add(woodSet.strippedWood().get());
            tag(Tags.Blocks.FENCES_WOODEN)
                .add(woodSet.fence().get());
            tag(Tags.Blocks.FENCE_GATES_WOODEN)
                .add(woodSet.fenceGate().get());
            if (woodSet.hasTreeBlocks()) {
                tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                    .add(woodSet.log().get());
                tag(BlockTags.LEAVES)
                    .add(woodSet.leaves().get());
                tag(BlockTags.SAPLINGS)
                    .add(woodSet.sapling().get());
                tag(BlockTags.FLOWER_POTS)
                    .add(woodSet.pottedSapling().get());
            }
        }

        tag(BlockTags.DIRT)
            .add(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get(), ModTerrainBlocks.SCORCHED_DIRT.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get(), ModTerrainBlocks.SCORCHED_DIRT.get());
        tag(BlockTags.DEAD_BUSH_MAY_PLACE_ON)
            .add(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get(), ModTerrainBlocks.SCORCHED_DIRT.get());
    }
}
