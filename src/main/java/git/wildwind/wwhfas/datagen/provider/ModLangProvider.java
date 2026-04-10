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
            addWoodSet(ModBlocks.CINDER, ModItems.CINDER, "Cinder");
            addWoodSet(ModBlocks.EMBER, ModItems.EMBER, "Ember");
            addBlockPropertyBookTranslations();
            return;
        }

        if ("zh_cn".equals(this.locale)) {
            add("mod.wwhfas.name", "原野之风：炉焰与渔歌");
            addWoodSet(ModBlocks.CINDER, ModItems.CINDER, "焚烬木");
            addWoodSet(ModBlocks.EMBER, ModItems.EMBER, "灵焰木");
            addBlockPropertyBookTranslations();
        }
    }

    private void addWoodSet(ModBlocks.WoodSet woodSet, ModItems.WoodItems woodItems, String baseName) {
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
        addItem(woodItems.boat(), baseName + suffix(" Boat", "船"));
        addItem(woodItems.chestBoat(), baseName + suffix(" Chest Boat", "运输船"));
    }


    private String suffix(String english, String chinese) {
        return "en_us".equals(this.locale) ? english : chinese;
    }

    private void addBlockPropertyBookTranslations() {
        add("message.wwhfas.block_property_book.created", text("Generated a block property book.", "已生成方块属性书。"));

        add("book.wwhfas.block_property.section.basic", text("[Basic]", "[基础属性]"));
        add("book.wwhfas.block_property.section.behavior", text("[Behavior]", "[行为属性]"));
        add("book.wwhfas.block_property.section.state", text("[State]", "[当前状态]"));
        add("book.wwhfas.block_property.section.extra", text("[Extra]", "[补充信息]"));

        add("book.wwhfas.block_property.entry.block_id", text("Block ID: %s", "方块ID: %s"));
        add("book.wwhfas.block_property.entry.position", text("Position: %s", "坐标: %s"));
        add("book.wwhfas.block_property.entry.item_id", text("Item ID: %s", "物品ID: %s"));
        add("book.wwhfas.block_property.entry.hardness", text("Hardness: %s", "硬度: %s"));
        add("book.wwhfas.block_property.entry.blast_resistance", text("Blast Resistance: %s", "爆炸抗性: %s"));
        add("book.wwhfas.block_property.entry.tool", text("Tool: %s", "合适工具: %s"));
        add("book.wwhfas.block_property.entry.needs_correct_tool", text("Needs Correct Tool: %s", "需要正确工具: %s"));
        add("book.wwhfas.block_property.entry.flammable", text("Flammable: %s", "可燃: %s"));
        add("book.wwhfas.block_property.entry.fire_odds", text("Ignite/Burn Odds: %s", "引燃/燃烧几率: %s"));
        add("book.wwhfas.block_property.entry.ignited_by_lava", text("Ignited By Lava: %s", "可被岩浆点燃: %s"));
        add("book.wwhfas.block_property.entry.light_level", text("Light Level: %s", "亮度: %s"));
        add("book.wwhfas.block_property.entry.map_color", text("Map Color: %s", "地图基色: %s"));
        add("book.wwhfas.block_property.entry.sound", text("Sound: %s", "声音类型: %s"));
        add("book.wwhfas.block_property.entry.random_ticks", text("Random Ticks: %s", "随机刻: %s"));
        add("book.wwhfas.block_property.entry.has_block_entity", text("Has Block Entity: %s", "方块实体: %s"));
        add("book.wwhfas.block_property.entry.piston_reaction", text("Piston Reaction: %s", "活塞行为: %s"));
        add("book.wwhfas.block_property.entry.occludes", text("Occludes: %s", "阻挡光照/面遮蔽: %s"));
        add("book.wwhfas.block_property.entry.full_collision", text("Full Collision Block: %s", "完整碰撞方块: %s"));
        add("book.wwhfas.block_property.entry.redstone_conductor", text("Redstone Conductor: %s", "红石导体: %s"));
        add("book.wwhfas.block_property.entry.signal_source", text("Signal Source: %s", "红石信号源: %s"));
        add("book.wwhfas.block_property.entry.suffocating", text("Suffocating: %s", "会导致窒息: %s"));
        add("book.wwhfas.block_property.entry.blocks_view", text("Blocks View: %s", "阻挡视线: %s"));
        add("book.wwhfas.block_property.entry.description_key", text("Description Key: %s", "描述键: %s"));
        add("book.wwhfas.block_property.entry.namespace", text("Namespace: %s", "命名空间: %s"));
        add("book.wwhfas.block_property.entry.path", text("Path: %s", "路径: %s"));
        add("book.wwhfas.block_property.entry.generated_by", text("Generated By: %s", "生成来源: %s"));

        add("book.wwhfas.block_property.yes", text("Yes", "是"));
        add("book.wwhfas.block_property.no", text("No", "否"));
        add("book.wwhfas.block_property.none", text("None", "无"));
        add("book.wwhfas.block_property.no_state_properties", text("No state properties.", "无可用状态属性。"));
        add("book.wwhfas.block_property.tool.axe", text("Axe", "斧"));
        add("book.wwhfas.block_property.tool.pickaxe", text("Pickaxe", "镐"));
        add("book.wwhfas.block_property.tool.shovel", text("Shovel", "锹"));
        add("book.wwhfas.block_property.tool.hoe", text("Hoe", "锄"));
        add("book.wwhfas.block_property.tool.none", text("None specified", "未指定"));
    }

    private String text(String english, String chinese) {
        return "en_us".equals(this.locale) ? english : chinese;
    }
}
