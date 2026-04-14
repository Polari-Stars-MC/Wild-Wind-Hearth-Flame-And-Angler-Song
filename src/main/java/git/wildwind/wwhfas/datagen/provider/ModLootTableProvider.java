package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(ModLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        );
    }

    private static final class ModLootSubProvider extends BlockLootSubProvider {
        private ModLootSubProvider(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected void generate() {
            for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
                dropSelf(woodSet.log().get());
                dropSelf(woodSet.wood().get());
                dropSelf(woodSet.strippedLog().get());
                dropSelf(woodSet.strippedWood().get());
                dropSelf(woodSet.planks().get());
                add((SlabBlock) woodSet.slab().get(), this::createSlabItemTable);
                dropSelf(woodSet.stairs().get());
                dropSelf(woodSet.fence().get());
                dropSelf(woodSet.fenceGate().get());
                add((DoorBlock) woodSet.door().get(), this::createDoorTable);
                dropSelf(woodSet.trapdoor().get());
                dropSelf(woodSet.pressurePlate().get());
                dropSelf(woodSet.button().get());
                dropSelf(woodSet.sign().get());
                dropSelf(woodSet.hangingSign().get());
                if (woodSet.hasTreeBlocks()) {
                    add((LeavesBlock) woodSet.leaves().get(), block -> createLeavesDrops(block, woodSet.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));
                    dropSelf(woodSet.sapling().get());
                    dropPottedContents(woodSet.pottedSapling().get());
                }
            }
            dropSelf(ModTerrainBlocks.SCORCHED_GRASS_BLOCK.get());
            dropSelf(ModTerrainBlocks.SCORCHED_DIRT.get());
            dropSelf(ModTerrainBlocks.SCORCHED_GRASS.get());
            dropSelf(ModTerrainBlocks.SCORCHED_TWIG.get());
            dropSelf(ModTerrainBlocks.SCORCHED_TWIG_WALL.get());
            dropSelf(ModTerrainBlocks.TINY_CACTUS.get());
            dropSelf(ModTerrainBlocks.FLETCHIING_TABLE.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Stream.concat(ModBlocks.BLOCKS.getEntries().stream(), ModTerrainBlocks.BLOCKS.getEntries().stream())
                .map(holder -> (Block) holder.get())
                .collect(Collectors.toList());
        }
    }
}
