package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.ModTerrainBlocks;
import git.wildwind.wwhfas.interaction.BlockPropertyBookFactory;
import git.wildwind.wwhfas.menu.ArrowFletchingMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraft.world.inventory.ContainerLevelAccess;

public final class ModCommonSetup {
    private ModCommonSetup() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ModCommonSetup::onCommonSetup);
        NeoForge.EVENT_BUS.addListener(ModCommonSetup::onBlockToolModification);
        NeoForge.EVENT_BUS.addListener(ModCommonSetup::onRightClickBlock);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
                registerFlammable(woodSet.log().get(), 5, 5);
                registerFlammable(woodSet.wood().get(), 5, 5);
                registerFlammable(woodSet.strippedLog().get(), 5, 5);
                registerFlammable(woodSet.strippedWood().get(), 5, 5);
                registerFlammable(woodSet.planks().get(), 5, 20);
                registerFlammable(woodSet.stairs().get(), 5, 20);
                registerFlammable(woodSet.slab().get(), 5, 20);
                registerFlammable(woodSet.fence().get(), 5, 20);
                registerFlammable(woodSet.fenceGate().get(), 5, 20);
                if (woodSet.hasTreeBlocks()) {
                    registerFlammable(woodSet.leaves().get(), 30, 60);
                }
            }
            registerFlammable(ModTerrainBlocks.SCORCHED_GRASS.get(), 60, 100);
            registerFlammable(ModTerrainBlocks.SCORCHED_TWIG.get(), 60, 100);
            registerFlammable(ModTerrainBlocks.SCORCHED_TWIG_WALL.get(), 60, 100);
        });
    }

    private static void registerFlammable(Block block, int igniteOdds, int burnOdds) {
        ((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds);
    }

    private static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.AXE_STRIP) {
            return;
        }

        BlockState strippedState = getStrippedState(event.getState());
        if (strippedState != null) {
            event.setFinalState(strippedState);
        }
    }

    private static BlockState getStrippedState(BlockState state) {
        for (ModBlocks.WoodSet woodSet : ModBlocks.WOOD_SETS) {
            if (state.is(woodSet.log().get())) {
                return copyLogState(state, woodSet.strippedLog().get());
            }
            if (state.is(woodSet.wood().get())) {
                return copyLogState(state, woodSet.strippedWood().get());
            }
        }
        return null;
    }

    private static BlockState copyLogState(BlockState originalState, Block strippedBlock) {
        BlockState strippedState = strippedBlock.defaultBlockState();
        if (originalState.hasProperty(RotatedPillarBlock.AXIS) && strippedState.hasProperty(RotatedPillarBlock.AXIS)) {
            strippedState = strippedState.setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
        }
        return strippedState;
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            if (shouldInspectBlock(player, event.getItemStack(), event.getHand() == InteractionHand.MAIN_HAND)) {
                event.setUseBlock(TriState.FALSE);
                event.setUseItem(TriState.FALSE);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            if (event.getHand() == InteractionHand.MAIN_HAND && player.level().getBlockState(event.getPos()).is(Blocks.FLETCHING_TABLE)) {
                event.setUseBlock(TriState.FALSE);
                event.setUseItem(TriState.FALSE);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!shouldInspectBlock(player, event.getItemStack(), event.getHand() == InteractionHand.MAIN_HAND)) {
            if (event.getHand() == InteractionHand.MAIN_HAND && player.level().getBlockState(event.getPos()).is(Blocks.FLETCHING_TABLE)) {
                openArrowFletchingMenu(serverPlayer, event.getPos());
                event.setUseBlock(TriState.FALSE);
                event.setUseItem(TriState.FALSE);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        Inventory inventory = player.getInventory();
        int bookSlot = findBlankBookSlot(inventory);
        if (bookSlot < 0) {
            return;
        }

        ItemStack writtenBook = BlockPropertyBookFactory.create(
            serverPlayer,
            player.level(),
            event.getPos(),
            player.level().getBlockState(event.getPos())
        );
        consumeBookAndGiveResult(inventory, player, bookSlot, writtenBook);
        player.displayClientMessage(Component.translatable("message.wwhfas.block_property_book.created"), true);
        player.containerMenu.broadcastChanges();

        event.setUseBlock(TriState.FALSE);
        event.setUseItem(TriState.FALSE);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static boolean shouldInspectBlock(Player player, ItemStack heldStack, boolean mainHandEvent) {
        if (!player.isShiftKeyDown() || !heldStack.is(Items.STICK)) {
            return false;
        }
        if (!mainHandEvent && player.getMainHandItem().is(Items.STICK)) {
            return false;
        }
        return findBlankBookSlot(player.getInventory()) >= 0;
    }

    private static int findBlankBookSlot(Inventory inventory) {
        int writableBookSlot = inventory.findSlotMatchingItem(new ItemStack(Items.WRITABLE_BOOK));
        if (writableBookSlot >= 0) {
            return writableBookSlot;
        }

        int bookSlot = inventory.findSlotMatchingItem(new ItemStack(Items.BOOK));
        if (bookSlot >= 0) {
            return bookSlot;
        }

        ItemStack offhandStack = inventory.offhand.get(0);
        if (offhandStack.is(Items.WRITABLE_BOOK) || offhandStack.is(Items.BOOK)) {
            return Inventory.SLOT_OFFHAND;
        }
        return -1;
    }

    private static void consumeBookAndGiveResult(Inventory inventory, Player player, int slot, ItemStack writtenBook) {
        ItemStack sourceStack = inventory.getItem(slot);
        sourceStack.shrink(1);
        if (sourceStack.isEmpty()) {
            inventory.setItem(slot, writtenBook);
            return;
        }

        if (!inventory.add(writtenBook)) {
            player.drop(writtenBook, false);
        }
    }

    private static void openArrowFletchingMenu(ServerPlayer player, net.minecraft.core.BlockPos pos) {
        player.openMenu(
            new SimpleMenuProvider(
                (containerId, inventory, targetPlayer) -> new ArrowFletchingMenu(
                    containerId,
                    inventory,
                    ContainerLevelAccess.create(targetPlayer.level(), pos)
                ),
                Component.translatable("container.wwhfas.arrow_fletching")
            ),
            pos
        );
    }
}
