package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.block.ModBlocks;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
                add((LeavesBlock) woodSet.leaves().get(), block -> createLeavesDrops(block, woodSet.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));
                dropSelf(woodSet.planks().get());
                add((SlabBlock) woodSet.slab().get(), this::createSlabItemTable);
                dropSelf(woodSet.stairs().get());
                dropSelf(woodSet.fence().get());
                dropSelf(woodSet.fenceGate().get());
                add((DoorBlock) woodSet.door().get(), this::createDoorTable);
                dropSelf(woodSet.trapdoor().get());
                dropSelf(woodSet.pressurePlate().get());
                dropSelf(woodSet.button().get());
                dropSelf(woodSet.sapling().get());
                dropPottedContents(woodSet.pottedSapling().get());
                dropSelf(woodSet.sign().get());
                dropSelf(woodSet.hangingSign().get());
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get()).collect(Collectors.toList());
        }
    }
}
