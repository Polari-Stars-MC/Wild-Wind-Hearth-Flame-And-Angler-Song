package git.wildwind.wwhfas.datagen.provider;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    private final String locale;
    public ModLangProvider(PackOutput output, String locale) {
        super(output, WildWindMod.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if ("en_us".equals(this.locale)) {
            add("mod.wwhfas.name", "Wild Wind: Hearth Flame & Angler Song");
            addWoodSet(ModBlocks.CINDER, "Cinder");
            addWoodSet(ModBlocks.EMBER, "Ember");
            return;
        }

        if ("zh_cn".equals(this.locale)) {
            add("mod.wwhfas.name", "原野之风：炉焰与渔歌");
            addWoodSet(ModBlocks.CINDER, "余烬木");
            addWoodSet(ModBlocks.EMBER, "炽焰木");
        }
    }

    private void addWoodSet(ModBlocks.WoodSet woodSet, String baseName) {
        addBlock(woodSet.log(), baseName + suffix(" Log", "原木"));
        addBlock(woodSet.wood(), baseName + suffix(" Wood", "木块"));
        addBlock(woodSet.strippedLog(), suffix("Stripped ", "去皮") + baseName + suffix(" Log", "原木"));
        addBlock(woodSet.strippedWood(), suffix("Stripped ", "去皮") + baseName + suffix(" Wood", "木块"));
        addBlock(woodSet.leaves(), baseName + suffix(" Leaves", "树叶"));
        addBlock(woodSet.planks(), baseName + suffix(" Planks", "木板"));
        addBlock(woodSet.stairs(), baseName + suffix(" Stairs", "楼梯"));
        addBlock(woodSet.slab(), baseName + suffix(" Slab", "台阶"));
        addBlock(woodSet.fence(), baseName + suffix(" Fence", "栅栏"));
        addBlock(woodSet.fenceGate(), baseName + suffix(" Fence Gate", "栅栏门"));
        addBlock(woodSet.door(), baseName + suffix(" Door", "门"));
        addBlock(woodSet.trapdoor(), baseName + suffix(" Trapdoor", "活板门"));
        addBlock(woodSet.pressurePlate(), baseName + suffix(" Pressure Plate", "压力板"));
        addBlock(woodSet.button(), baseName + suffix(" Button", "按钮"));
        addBlock(woodSet.sapling(), baseName + suffix(" Sapling", "树苗"));
        addBlock(woodSet.pottedSapling(), suffix("Potted ", "盆栽") + baseName + suffix(" Sapling", "树苗"));
        addBlock(woodSet.sign(), baseName + suffix(" Sign", "告示牌"));
        addBlock(woodSet.hangingSign(), baseName + suffix(" Hanging Sign", "悬挂式告示牌"));
    }


    private String suffix(String english, String chinese) {
        return "en_us".equals(this.locale) ? english : chinese;
    }
}
