package org.polaris2023.wild_wind.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.polaris2023.wild_wind.registry.ModFoliagePlacerTypes;

/**
 * 灵焰木树叶放置器喵~
 */
public class CinderFoliagePlacer extends FoliagePlacer {

	/**
	 * 灵焰木树叶放置器的编解码器喵~
	 */
	public static final MapCodec<CinderFoliagePlacer> CODEC =
			RecordCodecBuilder.mapCodec(instance ->
					foliagePlacerParts(instance)
							.apply(instance, CinderFoliagePlacer::new)
			);

	/**
	 * 创建灵焰木树叶放置器喵~
	 *
	 * @param radius 树叶半径提供器喵~
	 * @param offset 树叶偏移提供器喵~
	 */
	public CinderFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return ModFoliagePlacerTypes.CINDER.get();
	}

	@Override
	protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter,
								 RandomSource random, TreeConfiguration config,
								 int maxFreeTreeHeight, FoliageAttachment attachment,
								 int foliageHeight, int foliageRadius, int offset) {
		BlockPos trunkPos = attachment.pos();


	}
	



	/**
	 * 获取灵焰木树叶高度喵~
	 *
	 * @param random 随机源喵~
	 * @param height 树木高度喵~
	 * @param config 树木配置喵~
	 * @return 树叶高度喵~
	 */
	@Override
	public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
		return 1;
	}

	@Override
	protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
		return false;
	}
}
