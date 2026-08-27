package org.polaris2023.wild_wind.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.menu.ArrowFletchingMenu;

/**
 * 制箭台菜单的客户端界面喵~
 */
public final class ArrowFletchingScreen extends ItemCombinerScreen<ArrowFletchingMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WildWindMod.MOD_ID, "textures/gui/container/fletcher.png");

	/**
	 * 创建制箭台界面喵~
	 *
	 * @param menu 菜单实例喵~
	 * @param playerInventory 玩家背包喵~
	 * @param title 界面标题喵~
	 */
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
