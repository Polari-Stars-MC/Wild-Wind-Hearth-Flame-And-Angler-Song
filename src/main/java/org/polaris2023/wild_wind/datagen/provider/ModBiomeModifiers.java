package org.polaris2023.wild_wind.datagen.provider;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.registry.ModEntities;
import org.polaris2023.wild_wind.tag.ModBiomeTags;

import java.util.List;

// NOTE: 在ModDataGen中此类的使用之前被注释了，所以我暂时注释掉所有此类中因为被注释掉不会生效的内容。
/**
 * 定义群系修改器的数据生成内容喵~
 */
public final class ModBiomeModifiers {
	/**
	 * 为泥沼蟹添加生成规则的群系修改器键喵~
	 */
	public static final ResourceKey<BiomeModifier> ADD_MUDCRAB_SPAWNS = createKey("add_mudcrab_spawns");
	public static final ResourceKey<BiomeModifier> ADD_PIRANHA_SPAWNS = createKey("add_piranha_spawns");
	public static final ResourceKey<BiomeModifier> REMOVE_SPIDER_SPAWNS = createKey("remove_spider_spawns");
	public static final ResourceKey<BiomeModifier> ADD_CAVE_SPIDER_SPAWNS = createKey("cave_spider_spawns");
	/**
	 * 为挺水植物补丁添加生成规则的群系修改器键喵~
	 */
	public static final ResourceKey<BiomeModifier> ADD_EMERGENT_PLANT_PATCHES = createKey("add_emergent_plant_patches");


//	public static final ResourceKey<BiomeModifier> ADD_CINDER = createKey("add_cinder");
//	public static final ResourceKey<BiomeModifier> ADD_EMBER = createKey("add_ember");

	private ModBiomeModifiers() {
	}

	/**
	 * 向引导上下文注册群系修改器喵~
	 *
	 * @param context 群系修改器引导上下文喵~
	 */
	public static void bootstrap(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);

		context.register(
				ADD_EMERGENT_PLANT_PATCHES,
				new BiomeModifiers.AddFeaturesBiomeModifier(
						biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
						HolderSet.direct(
								features.getOrThrow(ModPlacedFeatures.PATCH_REEDS),
								features.getOrThrow(ModPlacedFeatures.PATCH_CATTAILS)
						),
						GenerationStep.Decoration.VEGETAL_DECORATION
				)
		);

		context.register(
				ADD_MUDCRAB_SPAWNS,
				new BiomeModifiers.AddSpawnsBiomeModifier(
						biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
						List.of(
								new MobSpawnSettings.SpawnerData(
										ModEntities.MUDCRAB.get(),
										8,
										1,
										2
								)
						)
				)
		);

		context.register(
				REMOVE_SPIDER_SPAWNS,
				new BiomeModifiers.RemoveSpawnsBiomeModifier(
						biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
						HolderSet.direct(EntityType.SPIDER.builtInRegistryHolder())
				)
		);

		context.register(
				ADD_CAVE_SPIDER_SPAWNS,
				new BiomeModifiers.AddSpawnsBiomeModifier(
						biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
						List.of(
								new MobSpawnSettings.SpawnerData(
										EntityType.CAVE_SPIDER,
										100,
										4,
										4
								)
						)
				)
		);

		context.register(
				ADD_PIRANHA_SPAWNS,
				new BiomeModifiers.AddSpawnsBiomeModifier(
						biomes.getOrThrow(ModBiomeTags.SPAWNS_PIRANHAS),
						List.of(
								new MobSpawnSettings.SpawnerData(
								ModEntities.PIRANHA.get(),
								8,
								1,
								5
								)
						)
				)
		);

		//		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
//
//		HolderSet.Named<Biome> overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);
//
//		context.register(
//				ADD_CINDER,
//				new BiomeModifiers.AddFeaturesBiomeModifier(
//						overworldBiomes,
//						HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CINDER)),
//						GenerationStep.Decoration.VEGETAL_DECORATION
//				)
//		);
//		context.register(
//				ADD_EMBER,
//				new BiomeModifiers.AddFeaturesBiomeModifier(
//						overworldBiomes,
//						HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EMBER)),
//						GenerationStep.Decoration.VEGETAL_DECORATION
//				)
//		);
	}

	private static ResourceKey<BiomeModifier> createKey(String name) {
		return ResourceKey.create(
				NeoForgeRegistries.Keys.BIOME_MODIFIERS,
				ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, name)
		);
	}
}