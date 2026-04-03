package git.wildwind.wwhfas.client;

import git.wildwind.wwhfas.client.screen.ModConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class ModClientHooks {
    private ModClientHooks() {
    }

    public static void register(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (container, parent) -> new ModConfigScreen(container, parent)
        );
    }
}
