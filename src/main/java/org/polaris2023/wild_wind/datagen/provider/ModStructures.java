package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.tag.ModBiomeTags;

import java.util.Map;

public class ModStructures {
    public static final ResourceKey<Structure> RUINED_CHAPEL = createKey("ruined_chapel");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(RUINED_CHAPEL, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(ModBiomeTags.HAS_STRUCTURE_RUINED_CHAPEL),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(ModStructureTemplatePools.RUINED_CHAPEL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true,
                Heightmap.Types.WORLD_SURFACE_WG
        ));
    }

    public static ResourceKey<Structure> createKey(String path) {
        return ResourceKey.create(Registries.STRUCTURE, WildWindMod.id(path));
    }
}
