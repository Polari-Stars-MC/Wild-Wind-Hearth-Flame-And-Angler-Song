package git.wildwind.wwhfas.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;

public class ModConfigScreen extends Screen {
    private final ModContainer modContainer;
    private final Screen modListScreen;

    public ModConfigScreen(ModContainer modContainer, Screen modListScreen) {
        super(Component.literal("Configuration"));
        this.modContainer = modContainer;
        this.modListScreen = modListScreen;
    }
}
