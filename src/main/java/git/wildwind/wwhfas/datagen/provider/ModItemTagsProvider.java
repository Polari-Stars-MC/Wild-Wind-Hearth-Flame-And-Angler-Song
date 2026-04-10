package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.registry.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ModBlockTagsProvider blockTagsProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTagsProvider.contentsGetter(), WildWindMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(net.minecraft.tags.BlockTags.LOGS, ItemTags.LOGS);
        copy(net.minecraft.tags.BlockTags.PLANKS, ItemTags.PLANKS);
        copy(net.minecraft.tags.BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(net.minecraft.tags.BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(net.minecraft.tags.BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(net.minecraft.tags.BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(net.minecraft.tags.BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(net.minecraft.tags.BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(net.minecraft.tags.BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(net.minecraft.tags.BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copy(net.minecraft.tags.BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        copy(net.minecraft.tags.BlockTags.LEAVES, ItemTags.LEAVES);
        copy(net.minecraft.tags.BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS);
        copy(Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS);
        copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
        copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

        for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
            TagKey<Item> logsTag = TagKey.create(
                net.minecraft.core.registries.Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, woodSet.name() + "_logs")
            );

            copy(
                TagKey.create(net.minecraft.core.registries.Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, woodSet.name() + "_logs")),
                logsTag
            );
        }

        for (ModItems.WoodItems woodItems : ModItems.WOOD_ITEMS) {
            tag(ItemTags.BOATS)
                .add(woodItems.boat().get());
            tag(ItemTags.CHEST_BOATS)
                .add(woodItems.chestBoat().get());
            tag(ItemTags.SIGNS)
                .add(woodItems.sign().get());
            tag(ItemTags.HANGING_SIGNS)
                .add(woodItems.hangingSign().get());
        }
    }
}
