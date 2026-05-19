package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.worldgen.tree.CinderFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


/**
 * 注册模组树叶放置器类型喵~
 */
public class ModFoliagePlacerTypes {
	/**
	 * 树叶放置器类型延迟注册器喵~
	 */
	public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, WildWindMod.MOD_ID);

	/**
	 * 灵焰木树叶放置器类型喵~
	 */
	public static final Supplier<FoliagePlacerType<CinderFoliagePlacer>> CINDER =
			FOLIAGE_PLACER_TYPE.register("cinder",
					() -> new FoliagePlacerType<>(CinderFoliagePlacer.CODEC));

	private ModFoliagePlacerTypes() {
	}
}
