package org.polaris2023.wwhfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.tag.ModBiomeTags;

import java.util.concurrent.CompletableFuture;

/**
 * 生成模组群系标签数据喵~
 */
public class ModBiomeTagsProvider extends BiomeTagsProvider {
	/**
	 * 创建群系标签提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param provider 注册表查询提供器喵~
	 * @param existingFileHelper 已有文件辅助器喵~
	 */
	public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, WildWindMod.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(ModBiomeTags.SPAWNS_WARM_VARIANT_CRABS)
				.add(Biomes.DESERT)
				.add(Biomes.WARM_OCEAN)
				.addTag(BiomeTags.IS_JUNGLE)
				.addTag(BiomeTags.IS_SAVANNA)
				.addTag(BiomeTags.IS_NETHER)
				.addTag(BiomeTags.IS_BADLANDS)
				.add(Biomes.MANGROVE_SWAMP);
		tag(ModBiomeTags.SPAWNS_COLD_VARIANT_CRABS)
				.add(Biomes.SNOWY_PLAINS)
				.add(Biomes.ICE_SPIKES)
				.add(Biomes.FROZEN_PEAKS)
				.add(Biomes.JAGGED_PEAKS)
				.add(Biomes.SNOWY_SLOPES)
				.add(Biomes.FROZEN_OCEAN)
				.add(Biomes.DEEP_FROZEN_OCEAN)
				.add(Biomes.GROVE)
				.add(Biomes.DEEP_DARK)
				.add(Biomes.FROZEN_RIVER)
				.add(Biomes.SNOWY_TAIGA)
				.add(Biomes.SNOWY_BEACH)
				.addTag(BiomeTags.IS_END);
		tag(ModBiomeTags.SPAWNS_PIRANHAS)
				.add(Biomes.JUNGLE)
				.addTag(Tags.Biomes.IS_SWAMP);
	}
}
