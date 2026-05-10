package git.wildwind.wwhfas.loot;

import com.google.common.collect.ImmutableList;
import git.wildwind.wwhfas.registry.ModPotions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.Consumer;

public class LootTableModifications {
    private static Object2ObjectOpenHashMap<ResourceKey<LootTable>, Consumer<LootTable>> modifications = new Object2ObjectOpenHashMap<>();

    static {
        registerModification(BuiltInLootTables.FLETCHER_GIFT, LootTableModifications::modifyFletcherGift);
    }

    private static void modifyFletcherGift(LootTable lootTable) {
        LootPool pool = lootTable.getPool("main");
        if (pool == null) return;

        pool.entries = ImmutableList.<LootPoolEntryContainer>builder()
                .addAll(pool.entries)
                .add(
                        LootItem.lootTableItem(Items.TIPPED_ARROW)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                .apply(SetPotionFunction.setPotion(ModPotions.REACH))
                                .build()
                )
                .build();
    }

    public static void applyModification(ResourceKey<LootTable> key, LootTable lootTable) {
        var iter = modifications.object2ObjectEntrySet().fastIterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            ResourceKey<LootTable> lootKey = entry.getKey();
            if (lootKey.equals(key)) {
                entry.getValue().accept(lootTable);
                modifications.remove(lootKey);
                break;
            }
        }

        if (!hasModifications()) {
            modifications = null;
        }
    }

    public static boolean hasModifications() {
        return modifications != null && !modifications.isEmpty();
    }

    private static void registerModification(ResourceKey<LootTable> key, Consumer<LootTable> modification) {
        modifications.put(key, modification);
    }
}
