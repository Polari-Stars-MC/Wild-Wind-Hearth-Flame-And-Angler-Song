package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.registry.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var compostables = builder(NeoForgeDataMaps.COMPOSTABLES);
        for (ModItems.WoodItems woodItems : ModItems.WOOD_ITEMS) {
            compostables.add(woodItems.leaves().get().builtInRegistryHolder(), new Compostable(0.3f, false), false);
            compostables.add(woodItems.sapling().get().builtInRegistryHolder(), new Compostable(0.3f, false), false);
        }
    }
}