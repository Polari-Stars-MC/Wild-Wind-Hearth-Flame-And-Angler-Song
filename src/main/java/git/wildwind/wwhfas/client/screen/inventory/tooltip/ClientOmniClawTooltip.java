package git.wildwind.wwhfas.client.screen.inventory.tooltip;

import git.wildwind.wwhfas.item.component.OmniClawTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ClientOmniClawTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot");
    private static final ResourceLocation[] EMPTY_SLOT_TOOLS_TEXTURE = new ResourceLocation[] {
            ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
            ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
            ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
            ResourceLocation.withDefaultNamespace("item/empty_slot_hoe")
    };
    private static final int SLOT_WIDTH = 18;
    private static final int SLOT_HEIGHT = 20;
    private static final int SLOT_COUNT = 4;
    private static final int MARGIN = 1;
    private final OmniClawTools tools;

    public ClientOmniClawTooltip(OmniClawTools tools) {
        this.tools = tools;
    }

    @Override
    public int getHeight() {
        return getBackgroundHeight() + 4;
    }

    @Override
    public int getWidth(Font font) {
        return SLOT_WIDTH * SLOT_COUNT + MARGIN * 2;
    }

    private int getBackgroundHeight() {
        return SLOT_HEIGHT + MARGIN * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.getWidth(font), this.getBackgroundHeight());
        y += MARGIN;
        x += MARGIN;
        for (int i = 0; i < 4; i++) {
            guiGraphics.blitSprite(SLOT_SPRITE, x, y, SLOT_WIDTH, SLOT_HEIGHT);

            ItemStack stack = this.tools.getTools().get(i);
            if (stack.isEmpty()) {
                TextureAtlasSprite sprite = Minecraft.getInstance()
                        .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(EMPTY_SLOT_TOOLS_TEXTURE[i]);
                guiGraphics.blit(x + 1, y + 1, 0, 16, 16, sprite);
            } else {
                guiGraphics.renderItem(stack, x + 1, y + 1);
                guiGraphics.renderItemDecorations(font, stack, x + 1, y + 1);
                if (i == this.tools.getLastSelected()) {
                    AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + 1, y + 1, 0);
                }
            }

            x += SLOT_WIDTH;
        }
    }
}
