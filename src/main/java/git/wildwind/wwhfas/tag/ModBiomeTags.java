package git.wildwind.wwhfas.tag;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface ModBiomeTags {
    TagKey<Biome> SPAWNS_WARM_VARIANT_CRABS = create("spawns_warm_variant_crabs");
    TagKey<Biome> SPAWNS_COLD_VARIANT_CRABS = create("spawns_cold_variant_crabs");


    static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, WildWindMod.id(path));
    }
}
