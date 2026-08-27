package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.registry.ModEntities;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

/**
 * 生成模组实体类型标签数据喵~
 */
public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
	/**
	 * 创建实体类型标签提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param provider 注册表查询提供器喵~
	 * @param existingFileHelper 已有文件辅助器喵~
	 */
	public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, WildWindMod.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(EntityTypeTags.AQUATIC)
				.add(ModEntities.MUDCRAB.get())
				.add(ModEntities.PIRANHA.get());
		tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
				.add(ModEntities.MUDCRAB.get())
				.add(ModEntities.PIRANHA.get());
		tag(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH)
				.add(ModEntities.MUDCRAB.get())
				.add(ModEntities.PIRANHA.get());
		tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
				.add(ModEntities.PIRANHA.get());
	}
}
