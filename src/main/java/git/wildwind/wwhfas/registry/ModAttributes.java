package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, WildWindMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> EXTRA_ITEM_PICKUP_RANGE =
            ATTRIBUTES.register("extra_item_pickup_range", () -> new RangedAttribute(
                    "attributes.wwhfas.extra_item_pickup_range",
                    0.0,
                    0.0,
                    10.0
            ));
}
