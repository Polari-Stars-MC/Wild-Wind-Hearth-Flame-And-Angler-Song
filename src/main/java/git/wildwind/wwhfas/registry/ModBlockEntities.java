package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.block.ModBlocks;
import git.wildwind.wwhfas.block.entity.ModHangingSignBlockEntity;
import git.wildwind.wwhfas.block.entity.ModSignBlockEntity;
import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Function;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WildWindMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> SIGN =
        BLOCK_ENTITY_TYPES.register(
            "sign",
            () -> BlockEntityType.Builder.of(ModSignBlockEntity::new, collectBlocks(ModBlocks.WoodSet::sign, ModBlocks.WoodSet::wallSign)).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN =
        BLOCK_ENTITY_TYPES.register(
            "hanging_sign",
            () -> BlockEntityType.Builder.of(
                ModHangingSignBlockEntity::new,
                collectBlocks(ModBlocks.WoodSet::hangingSign, ModBlocks.WoodSet::wallHangingSign)
            ).build(null)
        );

    private ModBlockEntities() {
    }

    public static void register(IEventBus modBus) {
        BLOCK_ENTITY_TYPES.register(modBus);
    }

    @SafeVarargs
    private static Block[] collectBlocks(Function<ModBlocks.WoodSet, DeferredHolder<Block, Block>>... accessors) {
        return ModBlocks.WOOD_SETS.stream()
                .flatMap(woodSet -> Arrays.stream(accessors).map(accessor -> accessor.apply(woodSet).get()))
                .toArray(Block[]::new);
    }
}
