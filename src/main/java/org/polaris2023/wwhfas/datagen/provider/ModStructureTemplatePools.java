package org.polaris2023.wwhfas.datagen.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import org.polaris2023.wwhfas.WildWindMod;

import java.util.List;

public class ModStructureTemplatePools {
    public static final ResourceKey<StructureTemplatePool> RUINED_CHAPEL = createKey("ruined_chapel");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(RUINED_CHAPEL, new StructureTemplatePool(
                pools.getOrThrow(Pools.EMPTY),
                List.of(
                        new Pair<>(SinglePoolElement.single(
                                WildWindMod.id("ruined_chapel").toString(),
                                LiquidSettings.APPLY_WATERLOGGING
                        ).apply(StructureTemplatePool.Projection.RIGID), 1)
                )
        ));
    }

    public static ResourceKey<StructureTemplatePool> createKey(String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, WildWindMod.id(path));
    }
}
