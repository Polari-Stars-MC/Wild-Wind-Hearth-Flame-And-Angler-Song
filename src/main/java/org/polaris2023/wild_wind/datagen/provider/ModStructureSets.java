package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.polaris2023.wild_wind.WildWindMod;

import java.util.Optional;

public class ModStructureSets {
    public static final ResourceKey<StructureSet> RUINED_CHAPEL = createKey("ruined_chapel");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);

        context.register(RUINED_CHAPEL, new StructureSet(
                structures.getOrThrow(ModStructures.RUINED_CHAPEL),
                new RandomSpreadStructurePlacement(
                        Vec3i.ZERO,
                        StructurePlacement.FrequencyReductionMethod.DEFAULT,
                        0.35f,
                        321053241,
                        Optional.of(new StructurePlacement.ExclusionZone(
                                structureSets.getOrThrow(BuiltinStructureSets.VILLAGES),
                                10
                        )),
                        32,
                        8,
                        RandomSpreadType.LINEAR
                )
        ));
    }

    public static ResourceKey<StructureSet> createKey(String path) {
        return ResourceKey.create(Registries.STRUCTURE_SET, WildWindMod.id(path));
    }
}
