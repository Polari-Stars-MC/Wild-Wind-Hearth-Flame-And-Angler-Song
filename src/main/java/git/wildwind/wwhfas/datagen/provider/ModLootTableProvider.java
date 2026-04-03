package git.wildwind.wwhfas.datagen.provider;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(ModLootSubProvider::new, LootContextParamSets.BLOCK))
        );
    }

    private static final class ModLootSubProvider implements LootTableSubProvider {
        private ModLootSubProvider(HolderLookup.Provider lookupProvider) {
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        }
    }
}
