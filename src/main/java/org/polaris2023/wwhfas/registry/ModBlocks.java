package org.polaris2023.wwhfas.registry;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.block.*;
import org.polaris2023.wwhfas.block.sign.ModCeilingHangingSignBlock;
import org.polaris2023.wwhfas.block.sign.ModStandingSignBlock;
import org.polaris2023.wwhfas.block.sign.ModWallHangingSignBlock;
import org.polaris2023.wwhfas.block.sign.ModWallSignBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 模组方块注册入口，负责注册基础植物方块与木材套装方块喵~
 */
public final class ModBlocks {
	/**
	 * 模组方块延迟寄存器喵~
	 */
	public static final DeferredRegister.Blocks BLOCKS =
			DeferredRegister.createBlocks(WildWindMod.MOD_ID);

	private static final BlockBehaviour.StatePredicate ALWAYS = (BlockState state, BlockGetter blockGetter, BlockPos pos) -> true;
	private static final BlockBehaviour.StatePredicate NEVER = (BlockState state, BlockGetter blockGetter, BlockPos pos) -> false;

	/**
	 * 芦苇方块喵~
	 */
	public static final DeferredBlock<Block> REEDS =
			BLOCKS.register("reeds", () -> new EmergentPlantBlock(
					BlockBehaviour.Properties.of()
							.mapColor(MapColor.TERRACOTTA_YELLOW)
							.replaceable()
							.noCollission()
							.instabreak()
							.sound(SoundType.GRASS)
							.offsetType(BlockBehaviour.OffsetType.XZ)
							.ignitedByLava()
							.pushReaction(PushReaction.DESTROY)
			));

	/**
	 * 香蒲方块喵~
	 */
	public static final DeferredBlock<Block> CATTAILS =
			BLOCKS.register("cattails", () -> new EmergentPlantBlock(
					BlockBehaviour.Properties.of()
							.mapColor(MapColor.TERRACOTTA_WHITE)
							.replaceable()
							.noCollission()
							.instabreak()
							.sound(SoundType.GRASS)
							.offsetType(BlockBehaviour.OffsetType.XZ)
							.ignitedByLava()
							.pushReaction(PushReaction.DESTROY)
			));

	/**
	 * 蜘蛛卵方块
	 */
	public static final DeferredBlock<SpiderCocoonBlock> SPIDER_COCOON =
			BLOCKS.register("spider_cocoon", () -> new SpiderCocoonBlock(
					BlockBehaviour.Properties.of()
							.strength(4)
							.mapColor(MapColor.WOOL)
							.noOcclusion()
							.isSuffocating(NEVER)
							.isRedstoneConductor(NEVER)
							.pushReaction(PushReaction.DESTROY)
							.ignitedByLava()
			));

	/**
	 * 悬挂蜘蛛卵方块
	 */
	public static final DeferredBlock<SpiderCocoonBlock> SPIDER_COCOON_HANGING =
			BLOCKS.register("spider_cocoon_hanging", () -> new HangingSpiderCocoonBlock(
					BlockBehaviour.Properties.of()
							.strength(4)
							.mapColor(MapColor.WOOL)
							.noOcclusion()
							.isSuffocating(NEVER)
							.isRedstoneConductor(NEVER)
							.pushReaction(PushReaction.DESTROY)
							.ignitedByLava()
			));

	/**
	 * 灰烬木套装方块集合喵~
	 */
	public static final WoodSet CINDER = registerWoodSet(
			"cinder",
			MapColor.PODZOL,
			MapColor.COLOR_BROWN,
			MapColor.COLOR_BROWN,
			ModTreeGrower.CINDER,
			ModBlockSetType.CINDER,
			ModWoodType.CINDER
	);
	/**
	 * 余烬木套装方块集合喵~
	 */
	public static final WoodSet EMBER = registerWoodSet(
			"ember",
			MapColor.COLOR_ORANGE,
			MapColor.TERRACOTTA_ORANGE,
			MapColor.COLOR_ORANGE,
			ModTreeGrower.EMBER,
			ModBlockSetType.EMBER,
			ModWoodType.EMBER
	);
	/**
	 * 杜鹃木套装方块集合喵~
	 */
	public static final WoodSet AZALEA = registerWoodSet(
			"azalea",
			MapColor.COLOR_PINK,
			MapColor.COLOR_PINK,
			MapColor.COLOR_PINK,
			ModBlockSetType.AZALEA,
			ModWoodType.AZALEA
	);

	/**
	 * 全部木材套装方块集合喵~
	 */
	public static final List<WoodSet> WOOD_SETS = List.of(CINDER, EMBER, AZALEA);

	private ModBlocks() {
	}

	/**
	 * 向事件总线注册方块延迟寄存器喵~
	 *
	 * @param modBus 模组事件总线喵~
	 */
	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
	}

	private static WoodSet registerWoodSet(
			String name,
			MapColor barkColor,
			MapColor woodColor,
			MapColor plankColor,
			BlockSetType blockSetType,
			WoodType woodType
	) {
		return registerWoodSet(name, barkColor, woodColor, plankColor, null, blockSetType, woodType);
	}

	private static WoodSet registerWoodSet(
			String name,
			MapColor barkColor,
			MapColor woodColor,
			MapColor plankColor,
			@Nullable TreeGrower treeGrower,
			BlockSetType blockSetType,
			WoodType woodType
	) {
		DeferredHolder<Block, Block> log = BLOCKS.register(name + "_log", () -> log(MapColor.WOOD, barkColor));
		DeferredHolder<Block, Block> wood = BLOCKS.register(name + "_wood", () -> log(barkColor, barkColor));
		DeferredHolder<Block, Block> strippedLog = BLOCKS.register(
				"stripped_" + name + "_log",
				() -> log(MapColor.WOOD, woodColor)
		);
		DeferredHolder<Block, Block> strippedWood = BLOCKS.register(
				"stripped_" + name + "_wood",
				() -> log(woodColor, woodColor)
		);
		DeferredHolder<Block, Block> leaves = treeGrower == null ? null : BLOCKS.register(name + "_leaves", ModBlocks::leaves);
		DeferredHolder<Block, Block> planks = BLOCKS.register(name + "_planks", () -> planks(plankColor));
		DeferredHolder<Block, Block> stairs = BLOCKS.register(name + "_stairs", () -> legacyStair(planks.get()));
		DeferredHolder<Block, Block> slab = BLOCKS.register(name + "_slab", () -> slab(plankColor));
		DeferredHolder<Block, Block> fence = BLOCKS.register(name + "_fence", () -> fence(plankColor));
		DeferredHolder<Block, Block> fenceGate = BLOCKS.register(name + "_fence_gate", () -> fenceGate(woodType, plankColor));
		DeferredHolder<Block, Block> door = BLOCKS.register(name + "_door", () -> door(blockSetType, plankColor));
		DeferredHolder<Block, Block> trapdoor = BLOCKS.register(name + "_trapdoor", () -> trapdoor(blockSetType, plankColor));
		DeferredHolder<Block, Block> pressurePlate = BLOCKS.register(
				name + "_pressure_plate",
				() -> pressurePlate(blockSetType, plankColor)
		);
		DeferredHolder<Block, Block> button = BLOCKS.register(name + "_button", () -> woodenButton(blockSetType));
		DeferredHolder<Block, Block> sapling = treeGrower == null ? null : BLOCKS.register(name + "_sapling", () -> sapling(treeGrower));
		DeferredHolder<Block, Block> pottedSapling = sapling == null
				? null
				: BLOCKS.register("potted_" + name + "_sapling", () -> pottedSapling(sapling));
		DeferredHolder<Block, Block> sign = BLOCKS.register(name + "_sign", () -> sign(woodType, plankColor));
		DeferredHolder<Block, Block> wallSign = BLOCKS.register(name + "_wall_sign", () -> wallSign(woodType, plankColor, sign));
		DeferredHolder<Block, Block> hangingSign = BLOCKS.register(
				name + "_hanging_sign",
				() -> hangingSign(woodType, plankColor)
		);
		DeferredHolder<Block, Block> wallHangingSign = BLOCKS.register(
				name + "_wall_hanging_sign",
				() -> wallHangingSign(woodType, plankColor, hangingSign)
		);

		return new WoodSet(
				name,
				log,
				wood,
				strippedLog,
				strippedWood,
				leaves,
				planks,
				stairs,
				slab,
				fence,
				fenceGate,
				door,
				trapdoor,
				pressurePlate,
				button,
				sapling,
				pottedSapling,
				sign,
				wallSign,
				hangingSign,
				wallHangingSign
		);
	}

	private static Block log(MapColor topMapColor, MapColor sideMapColor) {
		return new RotatedPillarBlock(
				BlockBehaviour.Properties.of()
						.mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor)
						.instrument(NoteBlockInstrument.BASS)
						.strength(2.0F)
						.sound(SoundType.WOOD)
						.ignitedByLava()
		);
	}

	private static Block leaves() {
		return new LeavesBlock(
				BlockBehaviour.Properties.of()
						.mapColor(MapColor.PLANT)
						.strength(0.2F)
						.randomTicks()
						.sound(SoundType.GRASS)
						.noOcclusion()
						.isValidSpawn(Blocks::ocelotOrParrot)
						.isSuffocating(NEVER)
						.isViewBlocking(NEVER)
						.ignitedByLava()
						.pushReaction(PushReaction.DESTROY)
						.isRedstoneConductor(NEVER)
		);
	}

	private static Block planks(MapColor mapColor) {
		return new Block(baseWoodProperties(mapColor));
	}

	private static Block legacyStair(Block baseBlock) {
		return new StairBlock(baseBlock.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(baseBlock));
	}

	private static Block slab(MapColor mapColor) {
		return new SlabBlock(baseWoodProperties(mapColor));
	}

	private static Block fence(MapColor mapColor) {
		return new FenceBlock(baseWoodProperties(mapColor));
	}

	private static Block fenceGate(WoodType woodType, MapColor mapColor) {
		return new FenceGateBlock(woodType, baseWoodProperties(mapColor));
	}

	private static Block door(BlockSetType blockSetType, MapColor mapColor) {
		return new DoorBlock(
				blockSetType,
				BlockBehaviour.Properties.of()
						.mapColor(mapColor)
						.instrument(NoteBlockInstrument.BASS)
						.strength(3.0F)
						.sound(SoundType.WOOD)
						.noOcclusion()
						.ignitedByLava()
						.pushReaction(PushReaction.DESTROY)
		);
	}

	private static Block trapdoor(BlockSetType blockSetType, MapColor mapColor) {
		return new TrapDoorBlock(blockSetType, baseWoodProperties(mapColor).noOcclusion());
	}

	private static Block pressurePlate(BlockSetType blockSetType, MapColor mapColor) {
		return new PressurePlateBlock(blockSetType, baseWoodProperties(mapColor));
	}

	private static Block woodenButton(BlockSetType type) {
		return new ButtonBlock(
				type,
				30,
				BlockBehaviour.Properties.of()
						.noCollission()
						.strength(0.5F)
						.sound(SoundType.WOOD)
						.pushReaction(PushReaction.DESTROY)
		);
	}

	private static Block sapling(TreeGrower treeGrower) {
		return new SaplingBlock(
				treeGrower,
				BlockBehaviour.Properties.of()
						.mapColor(MapColor.PLANT)
						.noCollission()
						.randomTicks()
						.instabreak()
						.sound(SoundType.GRASS)
						.pushReaction(PushReaction.DESTROY)
		);
	}

	private static Block pottedSapling(DeferredHolder<Block, Block> sapling) {
		return new FlowerPotBlock(
				() -> (FlowerPotBlock) Blocks.FLOWER_POT,
				sapling,
				BlockBehaviour.Properties.of()
						.instabreak()
						.noOcclusion()
						.pushReaction(PushReaction.DESTROY)
		);
	}

	private static Block sign(WoodType woodType, MapColor mapColor) {
		return new ModStandingSignBlock(
				woodType,
				BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SIGN).mapColor(mapColor),
				() -> ModBlockEntities.SIGN.get()
		);
	}

	private static Block wallSign(WoodType woodType, MapColor mapColor, DeferredHolder<Block, Block> sign) {
		return new ModWallSignBlock(
				woodType,
				BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_SIGN).mapColor(mapColor).lootFrom(sign),
				() -> ModBlockEntities.SIGN.get()
		);
	}

	private static Block hangingSign(WoodType woodType, MapColor mapColor) {
		return new ModCeilingHangingSignBlock(
				woodType,
				BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN).mapColor(mapColor),
				() -> ModBlockEntities.HANGING_SIGN.get()
		);
	}

	private static Block wallHangingSign(
			WoodType woodType,
			MapColor mapColor,
			DeferredHolder<Block, Block> hangingSign
	) {
		return new ModWallHangingSignBlock(
				woodType,
				BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(mapColor).lootFrom(hangingSign),
				() -> ModBlockEntities.HANGING_SIGN.get()
		);
	}

	private static BlockBehaviour.Properties baseWoodProperties(MapColor mapColor) {
		return BlockBehaviour.Properties.of()
				.mapColor(mapColor)
				.instrument(NoteBlockInstrument.BASS)
				.strength(2.0F, 3.0F)
				.sound(SoundType.WOOD)
				.ignitedByLava();
	}

	/**
	 * 木材套装方块集合定义喵~
	 *
	 * @param name 木材名称喵~
	 * @param log 原木方块喵~
	 * @param wood 树皮方块喵~
	 * @param strippedLog 去皮原木方块喵~
	 * @param strippedWood 去皮树皮方块喵~
	 * @param leaves 树叶方块喵~
	 * @param planks 木板方块喵~
	 * @param stairs 楼梯方块喵~
	 * @param slab 台阶方块喵~
	 * @param fence 栅栏方块喵~
	 * @param fenceGate 栅栏门方块喵~
	 * @param door 木门方块喵~
	 * @param trapdoor 活板门方块喵~
	 * @param pressurePlate 压力板方块喵~
	 * @param button 按钮方块喵~
	 * @param sapling 树苗方块喵~
	 * @param pottedSapling 盆栽树苗方块喵~
	 * @param sign 站立告示牌方块喵~
	 * @param wallSign 墙上告示牌方块喵~
	 * @param hangingSign 悬挂告示牌方块喵~
	 * @param wallHangingSign 墙上悬挂告示牌方块喵~
	 */
	public record WoodSet(
			String name,
			DeferredHolder<Block, Block> log,
			DeferredHolder<Block, Block> wood,
			DeferredHolder<Block, Block> strippedLog,
			DeferredHolder<Block, Block> strippedWood,
			@Nullable DeferredHolder<Block, Block> leaves,
			DeferredHolder<Block, Block> planks,
			DeferredHolder<Block, Block> stairs,
			DeferredHolder<Block, Block> slab,
			DeferredHolder<Block, Block> fence,
			DeferredHolder<Block, Block> fenceGate,
			DeferredHolder<Block, Block> door,
			DeferredHolder<Block, Block> trapdoor,
			DeferredHolder<Block, Block> pressurePlate,
			DeferredHolder<Block, Block> button,
			@Nullable DeferredHolder<Block, Block> sapling,
			@Nullable DeferredHolder<Block, Block> pottedSapling,
			DeferredHolder<Block, Block> sign,
			DeferredHolder<Block, Block> wallSign,
			DeferredHolder<Block, Block> hangingSign,
			DeferredHolder<Block, Block> wallHangingSign
	) {
		/**
		 * 判断当前木材套装是否包含树木相关方块喵~
		 *
		 * @return 若包含树叶与树苗相关方块则返回 true 喵~
		 */
		public boolean hasTreeBlocks() {
			return leaves != null && sapling != null && pottedSapling != null;
		}
	}
}
