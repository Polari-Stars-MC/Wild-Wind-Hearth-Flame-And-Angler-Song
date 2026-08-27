package org.polaris2023.wild_wind.tag;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.polaris2023.wild_wind.WildWindMod;

/**
 * 定义模组物品标签喵~
 */
public interface ModItemTags {
	/**
	 * 泥沼蟹食物的物品标签喵~
	 */
	TagKey<Item> MUDCRAB_FOOD = ItemTags.create(WildWindMod.id("mudcrab_food"));
}
