package git.wildwind.wwhfas.client.screen;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.menu.ArrowFletchingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class ArrowFletchingScreen extends ItemCombinerScreen<ArrowFletchingMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/gui/container/fletcher.png");

    public ArrowFletchingScreen(ArrowFletchingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
        this.titleLabelX = 8;
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
        if (this.menu.hasAnyInputItem() && this.menu.shouldShowError()) {
            guiGraphics.blit(TEXTURE, x + 93, y + 23, 176, 0, 28, 21);
        }
    }
}
