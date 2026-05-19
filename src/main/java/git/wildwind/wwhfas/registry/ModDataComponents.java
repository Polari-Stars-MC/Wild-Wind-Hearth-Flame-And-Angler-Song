package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.item.component.OmniClawTools;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 注册模组数据组件类型喵~
 */
public final class ModDataComponents {
    /**
     * 数据组件延迟注册器喵~
     */
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WildWindMod.MOD_ID);

    /**
     * 箭尾数据组件喵~
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ARROW_TAIL =
        DATA_COMPONENTS.registerComponentType(
            "arrow_tail",
            builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC)
        );
    /**
     * 箭杆数据组件喵~
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ARROW_SHAFT =
        DATA_COMPONENTS.registerComponentType(
            "arrow_shaft",
            builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC)
        );
    /**
     * 箭头数据组件喵~
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ARROW_HEAD =
        DATA_COMPONENTS.registerComponentType(
            "arrow_head",
            builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC)
        );

    /**
     * 万用蟹钳工具集合数据组件喵~
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<OmniClawTools>> OMNI_CLAW_TOOLS =
            DATA_COMPONENTS.registerComponentType("omni_claw_tools",
                    builder -> builder
                            .persistent(OmniClawTools.CODEC)
                            .networkSynchronized(OmniClawTools.STREAM_CODEC)
    );

    private ModDataComponents() {
    }

    /**
     * 向模组事件总线注册数据组件类型喵~
     *
     * @param modBus 模组事件总线喵~
     */
    public static void register(IEventBus modBus) {
        DATA_COMPONENTS.register(modBus);
    }
}
