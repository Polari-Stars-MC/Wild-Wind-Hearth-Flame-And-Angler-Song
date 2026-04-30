package git.wildwind.wwhfas.tag;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface ModItemTags {
    TagKey<Item> CRAB_FOOD = ItemTags.create(WildWindMod.id("crab_food"));
}
