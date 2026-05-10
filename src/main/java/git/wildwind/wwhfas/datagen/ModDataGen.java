package git.wildwind.wwhfas.datagen;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.datagen.provider.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ModDataGen {
    private ModDataGen() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ModDataGen::gatherData);
    }

    private static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        var blockTags = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLangProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new ModLangProvider(output, "zh_cn"));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModDataMapProvider(output, lookupProvider));
        generator.addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(
                        output,
                        lookupProvider,
                        ModWorldGenProvider.BUILDER,
                        Set.of(WildWindMod.MOD_ID)
                )
        );
        generator.addProvider(
                event.includeServer(),
                new ModConfiguredFeatureProvider(output, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new ModItemTagsProvider(output, lookupProvider, blockTags, existingFileHelper)
        );
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModBiomeTagsProvider(output, lookupProvider, existingFileHelper));
    }
}
