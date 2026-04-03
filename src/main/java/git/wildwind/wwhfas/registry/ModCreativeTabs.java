package git.wildwind.wwhfas.registry;

import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WildWindMod.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WILD_WIND =
            CREATIVE_TABS.register("wild_wind", () -> CreativeModeTab.builder()
                    .title(Component.translatable("mod.wwhfas.name"))
                    .icon(() -> ModItems.CINDER.log().get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        ModItems.ITEMS.getEntries().forEach((item) -> output.accept(item.get().getDefaultInstance()));
                    })
                    .build()
            );
    public static void register(IEventBus modBus) {
        CREATIVE_TABS.register(modBus);
    }
}
