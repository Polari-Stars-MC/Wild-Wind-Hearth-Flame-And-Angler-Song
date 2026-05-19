package org.polaris2023.wwhfas.client;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.block.ModWoodType;
import org.polaris2023.wwhfas.client.screen.ArrowFletchingScreen;
import org.polaris2023.wwhfas.client.screen.inventory.tooltip.ClientOmniClawTooltip;
import org.polaris2023.wwhfas.item.component.OmniClawTools;
import org.polaris2023.wwhfas.network.payload.ServerBoundSelectOmniClawItemPayload;
import org.polaris2023.wwhfas.registry.ModBlockEntities;
import org.polaris2023.wwhfas.registry.ModDataComponents;
import org.polaris2023.wwhfas.registry.ModMenuTypes;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

/**
 * 客户端侧钩子与界面注册入口喵~
 */
@Mod(value = WildWindMod.MOD_ID, dist = Dist.CLIENT)
public class ModClientHooks {

	/**
	 * 注册客户端事件监听与配置界面扩展点喵~
	 *
	 * @param modBus 模组事件总线喵~
	 * @param container 当前模组容器喵~
	 */
	public ModClientHooks(IEventBus modBus, ModContainer container) {
		modBus.addListener(ModClientHooks::onClientSetup);
		modBus.addListener(ModClientHooks::onRegisterRenderers);
		modBus.addListener(ModClientHooks::onRegisterScreens);
		modBus.addListener(ModClientHooks::onRegisterClientTooltipComponent);
		NeoForge.EVENT_BUS.addListener(ModClientHooks::onScreenPreMouseScroll);
		NeoForge.EVENT_BUS.addListener(ModClientHooks::onTooltip);
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	/**
	 * 完成客户端初始化时注册木材类型喵~
	 *
	 * @param event 客户端初始化事件喵~
	 */
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			Sheets.addWoodType(ModWoodType.CINDER);
			Sheets.addWoodType(ModWoodType.EMBER);
			Sheets.addWoodType(ModWoodType.AZALEA);
		});
	}

	/**
	 * 注册方块实体渲染器喵~
	 *
	 * @param event 渲染器注册事件喵~
	 */
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.SIGN.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);
	}

	/**
	 * 注册容器界面喵~
	 *
	 * @param event 菜单界面注册事件喵~
	 */
	public static void onRegisterScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenuTypes.ARROW_FLETCHING.get(), ArrowFletchingScreen::new);
	}

	/**
	 * 为箭矢物品补充部件提示信息喵~
	 *
	 * @param event 物品提示事件喵~
	 */
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.is(Items.ARROW)) {
			return;
		}

		addPartTooltip(event, "tooltip.wwhfas.arrow.tail", stack.get(ModDataComponents.ARROW_TAIL.get()));
		addPartTooltip(event, "tooltip.wwhfas.arrow.shaft", stack.get(ModDataComponents.ARROW_SHAFT.get()));
		addPartTooltip(event, "tooltip.wwhfas.arrow.head", stack.get(ModDataComponents.ARROW_HEAD.get()));
	}

	/**
	 * 注册全能蟹钳的客户端提示组件工厂喵~
	 *
	 * @param event 客户端提示组件注册事件喵~
	 */
	public static void onRegisterClientTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(OmniClawTools.class, ClientOmniClawTooltip::new);
	}

	/**
	 * 处理容器界面中的滚轮选中工具逻辑喵~
	 *
	 * @param event 鼠标滚轮事件喵~
	 */
	public static void onScreenPreMouseScroll(ScreenEvent.MouseScrolled.Pre event) {
		if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;
		Slot slot = screen.getSlotUnderMouse();

		if (slot == null) return;
		ItemStack hoverItem = slot.getItem();

		if (!hoverItem.has(ModDataComponents.OMNI_CLAW_TOOLS)) return;
		OmniClawTools tools = hoverItem.get(ModDataComponents.OMNI_CLAW_TOOLS);

		if (tools.getNonEmptyItemCount() < 2) return;
		double y = event.getScrollDeltaY();
		int index = tools.getSelectedToolIndex();
		int delta = (int) Math.signum(y == 0 ? event.getScrollDeltaX() : -y);
		List<ItemStack> toolStacks = tools.getTools();

		int start = index;
		int size = toolStacks.size();
		do {
			index = (index + delta + size) % size;

			ItemStack stack = toolStacks.get(index);
			if (stack != null && !stack.isEmpty()) {
				break;
			}
		} while (index != start);

		PacketDistributor.sendToServer(new ServerBoundSelectOmniClawItemPayload(slot.index, index));
		event.setCanceled(true);
	}

	private static void addPartTooltip(ItemTooltipEvent event, String key, ResourceLocation material) {
		if (material == null) {
			return;
		}
		event.getToolTip().add(Component.translatable(key, material.toString()));
	}
}
