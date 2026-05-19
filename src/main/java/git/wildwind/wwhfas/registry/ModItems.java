package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import git.wildwind.wwhfas.item.OmniClawItem;
import git.wildwind.wwhfas.item.component.OmniClawTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/**
 * 模组物品注册入口，负责注册木材物品、功能物品与实体桶等内容喵~
 */
public final class ModItems {
    /**
     * 模组物品延迟寄存器喵~
     */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, WildWindMod.MOD_ID);

    /**
     * 生物桶在发射器中的默认放置行为喵~
     */
    public static final DefaultDispenseItemBehavior MOB_BUCKET_DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource blockSource, ItemStack stack) {
            DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) stack.getItem();
            BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
            Level level = blockSource.level();
            if (dispensiblecontaineritem.emptyContents(null, level, blockpos, null, stack)) {
                dispensiblecontaineritem.checkExtraContent(null, level, stack, blockpos);
                return this.consumeWithRemainder(blockSource, stack, new ItemStack(Items.BUCKET));
            } else {
                return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
            }
        }
    };

    /**
     * 灰烬木套装物品集合喵~
     */
    public static final WoodItems CINDER = registerWoodItems(ModBlocks.CINDER, ModBoatTypes.CINDER);
    /**
     * 余烬木套装物品集合喵~
     */
    public static final WoodItems EMBER = registerWoodItems(ModBlocks.EMBER, ModBoatTypes.EMBER);
    /**
     * 杜鹃木套装物品集合喵~
     */
    public static final WoodItems AZALEA = registerWoodItems(ModBlocks.AZALEA, ModBoatTypes.AZALEA);
    /**
     * 焦土草方块对应物品喵~
     */
    public static final DeferredHolder<Item, Item> SCORCHED_GRASS_BLOCK = blockItem(ModTerrainBlocks.SCORCHED_GRASS_BLOCK);
    /**
     * 焦土对应物品喵~
     */
    public static final DeferredHolder<Item, Item> SCORCHED_DIRT = blockItem(ModTerrainBlocks.SCORCHED_DIRT);
    /**
     * 焦草对应物品喵~
     */
    public static final DeferredHolder<Item, Item> SCORCHED_GRASS = blockItem(ModTerrainBlocks.SCORCHED_GRASS);
    /**
     * 焦枝对应物品喵~
     */
    public static final DeferredHolder<Item, Item> SCORCHED_TWIG = ITEMS.register(
        ModTerrainBlocks.SCORCHED_TWIG.getId().getPath(),
        () -> new StandingAndWallBlockItem(
            ModTerrainBlocks.SCORCHED_TWIG.get(),
            ModTerrainBlocks.SCORCHED_TWIG_WALL.get(),
            new Item.Properties(),
            Direction.DOWN
        )
    );
    /**
     * 小仙人掌对应物品喵~
     */
    public static final DeferredHolder<Item, Item> TINY_CACTUS = blockItem(ModTerrainBlocks.TINY_CACTUS);

    /**
     * 全部木材套装物品集合喵~
     */
    public static final List<WoodItems> WOOD_ITEMS = List.of(CINDER, EMBER, AZALEA);

    /**
     * 向事件总线注册物品延迟寄存器喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

        private static WoodItems registerWoodItems(ModBlocks.WoodSet woodSet, EnumProxy<Boat.Type> boatType) {
        DeferredHolder<Item, Item> log = blockItem(woodSet.log());
        DeferredHolder<Item, Item> wood = blockItem(woodSet.wood());
        DeferredHolder<Item, Item> strippedLog = blockItem(woodSet.strippedLog());
        DeferredHolder<Item, Item> strippedWood = blockItem(woodSet.strippedWood());
        DeferredHolder<Item, Item> leaves = blockItem(woodSet.leaves());
        DeferredHolder<Item, Item> planks = blockItem(woodSet.planks());
        DeferredHolder<Item, Item> stairs = blockItem(woodSet.stairs());
        DeferredHolder<Item, Item> slab = blockItem(woodSet.slab());
        DeferredHolder<Item, Item> fence = blockItem(woodSet.fence());
        DeferredHolder<Item, Item> fenceGate = blockItem(woodSet.fenceGate());
        DeferredHolder<Item, Item> door = ITEMS.register(
            woodSet.door().getId().getPath(),
            () -> new BlockItem(woodSet.door().get(), new Item.Properties())
        );
        DeferredHolder<Item, Item> trapdoor = blockItem(woodSet.trapdoor());
        DeferredHolder<Item, Item> pressurePlate = blockItem(woodSet.pressurePlate());
        DeferredHolder<Item, Item> button = blockItem(woodSet.button());
        DeferredHolder<Item, Item> sapling = blockItem(woodSet.sapling());
        DeferredHolder<Item, Item> sign = ITEMS.register(
            woodSet.name() + "_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), woodSet.sign().get(), woodSet.wallSign().get())
        );
        DeferredHolder<Item, Item> hangingSign = ITEMS.register(
            woodSet.name() + "_hanging_sign",
            () -> new HangingSignItem(
                woodSet.hangingSign().get(),
                woodSet.wallHangingSign().get(),
                new Item.Properties().stacksTo(16)
            )
        );
        DeferredHolder<Item, Item> boat = ITEMS.register(
            woodSet.name() + "_boat",
            () -> new BoatItem(false, boatType.getValue(), new Item.Properties().stacksTo(1))
        );
        DeferredHolder<Item, Item> chestBoat = ITEMS.register(
            woodSet.name() + "_chest_boat",
            () -> new BoatItem(true, boatType.getValue(), new Item.Properties().stacksTo(1))
        );

        return new WoodItems(
            woodSet.name(),
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
            sign,
            hangingSign,
            boat,
            chestBoat
        );
    }

    private static DeferredHolder<Item, Item> blockItem(DeferredHolder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> block) {
        if (block == null) {
            return null;
        }
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * 木材套装物品集合定义喵~
     *
     * @param name 木材名称喵~
     * @param log 原木物品喵~
     * @param wood 树皮物品喵~
     * @param strippedLog 去皮原木物品喵~
     * @param strippedWood 去皮树皮物品喵~
     * @param leaves 树叶物品喵~
     * @param planks 木板物品喵~
     * @param stairs 楼梯物品喵~
     * @param slab 台阶物品喵~
     * @param fence 栅栏物品喵~
     * @param fenceGate 栅栏门物品喵~
     * @param door 木门物品喵~
     * @param trapdoor 活板门物品喵~
     * @param pressurePlate 压力板物品喵~
     * @param button 按钮物品喵~
     * @param sapling 树苗物品喵~
     * @param sign 告示牌物品喵~
     * @param hangingSign 悬挂告示牌物品喵~
     * @param boat 小船物品喵~
     * @param chestBoat 运输船物品喵~
     */
    public record WoodItems(
        String name,
        DeferredHolder<Item, Item> log,
        DeferredHolder<Item, Item> wood,
        DeferredHolder<Item, Item> strippedLog,
        DeferredHolder<Item, Item> strippedWood,
        DeferredHolder<Item, Item> leaves,
        DeferredHolder<Item, Item> planks,
        DeferredHolder<Item, Item> stairs,
        DeferredHolder<Item, Item> slab,
        DeferredHolder<Item, Item> fence,
        DeferredHolder<Item, Item> fenceGate,
        DeferredHolder<Item, Item> door,
        DeferredHolder<Item, Item> trapdoor,
        DeferredHolder<Item, Item> pressurePlate,
        DeferredHolder<Item, Item> button,
        DeferredHolder<Item, Item> sapling,
        DeferredHolder<Item, Item> sign,
        DeferredHolder<Item, Item> hangingSign,
        DeferredHolder<Item, Item> boat,
        DeferredHolder<Item, Item> chestBoat
    ) {
        /**
         * 判断当前木材套装是否包含树木相关物品喵~
         *
         * @return 若包含树叶与树苗物品则返回 true 喵~
         */
        public boolean hasTreeItems() {
            return leaves != null && sapling != null;
        }
    }

    /**
     * 螃蟹刷怪蛋喵~
     */
    public static final DeferredHolder<Item, DeferredSpawnEggItem> CRAB_SPAWN_EGG =
            ITEMS.register("crab_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            ModEntities.CRAB,
                            0xFF3F7E8E,
                            0xFFF9A45A,
                            new Item.Properties()
                    ));


    /**
     * 螃蟹桶喵~
     */
    public static final DeferredHolder<Item, MobBucketItem> CRAB_BUCKET =
            ITEMS.register("crab_bucket", () -> registerMobBucket(
                    ModEntities.CRAB.get(),
                    Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY)
            );

    /**
     * 蟹钳物品喵~
     */
    public static final DeferredHolder<Item, Item> CRAB_CLAW =
            ITEMS.register("crab_claw",
                    () -> new Item(
                            new Item.Properties()
                    ));

    /**
     * 万用蟹钳物品喵~
     */
    public static final DeferredHolder<Item, Item> OMNI_CLAW =
            ITEMS.register("omni_claw",
                    () -> new OmniClawItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .attributes(OmniClawItem.createAttributes())
                                    .component(ModDataComponents.OMNI_CLAW_TOOLS, OmniClawTools.EMPTY)
                    ));

    /**
     * 芦苇物品喵~
     */
    public static final DeferredHolder<Item, Item> REEDS =
            ITEMS.register("reeds",
                    () -> new BlockItem(
                            ModBlocks.REEDS.get(),
                            new Item.Properties()
                    ));

    /**
     * 香蒲物品喵~
     */
    public static final DeferredHolder<Item, Item> CATTAILS =
            ITEMS.register("cattails",
                    () -> new BlockItem(
                            ModBlocks.CATTAILS.get(),
                            new Item.Properties()
                    ));

    private static MobBucketItem registerMobBucket(EntityType<?> type, Fluid fluid, SoundEvent soundEvent) {
        MobBucketItem item = new MobBucketItem(
                type,
                fluid,
                soundEvent,
                new Item.Properties().stacksTo(1)
        );

        DispenserBlock.registerBehavior(item, MOB_BUCKET_DISPENSE_ITEM_BEHAVIOR);
        return item;
    }
}
