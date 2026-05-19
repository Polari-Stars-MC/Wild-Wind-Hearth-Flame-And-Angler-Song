package git.wildwind.wwhfas.tag;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * 定义模组物品标签喵~
 */
public interface ModItemTags {
    /**
     * 螃蟹食物的物品标签喵~
     */
    TagKey<Item> CRAB_FOOD = ItemTags.create(WildWindMod.id("crab_food"));
}
