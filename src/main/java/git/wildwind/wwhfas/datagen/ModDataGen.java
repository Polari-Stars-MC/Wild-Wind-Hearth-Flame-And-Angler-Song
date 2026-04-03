package git.wildwind.wwhfas.datagen;

import git.wildwind.wwhfas.datagen.provider.ModBlockStateProvider;
import git.wildwind.wwhfas.datagen.provider.ModBlockTagsProvider;
import git.wildwind.wwhfas.datagen.provider.ModItemModelProvider;
import git.wildwind.wwhfas.datagen.provider.ModItemTagsProvider;
import git.wildwind.wwhfas.datagen.provider.ModLangProvider;
import git.wildwind.wwhfas.datagen.provider.ModLootTableProvider;
import git.wildwind.wwhfas.datagen.provider.ModRecipeProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class ModDataGen {
    private ModDataGen() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ModDataGen::gatherData);
    }

    private static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existing = event.getExistingFileHelper();

        var blockTags = new ModBlockTagsProvider(output, event.getLookupProvider(), existing);

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existing));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existing));
        generator.addProvider(event.includeClient(), new ModLangProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new ModLangProvider(output, "zh_cn"));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output));
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(
                event.includeServer(),
                new ModItemTagsProvider(output, event.getLookupProvider(), blockTags, existing)
        );
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, event.getLookupProvider()));
    }
}
