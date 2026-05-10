package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.entity.animal.CrabVariant;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, WildWindMod.MOD_ID);

    public static final Supplier<EntityDataSerializer<Holder<CrabVariant>>> CRAB_VARIANT =
            ENTITY_DATA_SERIALIZERS.register("crab_variant", () -> EntityDataSerializer.forValueType(CrabVariant.STREAM_CODEC));

    public static void register(IEventBus modBus) {
        ENTITY_DATA_SERIALIZERS.register(modBus);
    }
}
