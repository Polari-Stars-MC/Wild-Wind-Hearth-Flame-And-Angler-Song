package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WildWindMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
            registerWoodSet(woodSet);
        }
        registerTerrainItems();
    }

    private void registerWoodSet(ModBlocks.WoodSet woodSet) {
        String name = woodSet.name();

        withExistingParent(name + "_fence", mcLoc("block/fence_inventory"))
            .texture("texture", modLoc("block/" + name + "_planks"));
        withExistingParent(name + "_button", mcLoc("block/button_inventory"))
            .texture("texture", modLoc("block/" + name + "_planks"));
        withExistingParent(name + "_door", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + name + "_door"));
        if (woodSet.hasTreeBlocks()) {
            withExistingParent(name + "_sapling", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + name + "_sapling"));
        }
        withExistingParent(name + "_sign", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + name + "_sign"));
        withExistingParent(name + "_hanging_sign", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + name + "_hanging_sign"));
        withExistingParent(name + "_boat", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + name + "_boat"));
        withExistingParent(name + "_chest_boat", mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + name + "_chest_boat"));
    }

    private void registerTerrainItems() {
        withExistingParent(ModTerrainBlocks.SCORCHED_GRASS.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("block/scorched_grass"));
        withExistingParent(ModTerrainBlocks.SCORCHED_TWIG.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("block/scorched_twig"));
        withExistingParent(ModTerrainBlocks.TINY_CACTUS.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("block/tiny_cactus"));
    }
}
