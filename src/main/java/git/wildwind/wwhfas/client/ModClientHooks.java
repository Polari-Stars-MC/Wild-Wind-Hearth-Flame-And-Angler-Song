package git.wildwind.wwhfas.client;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.block.ModWoodType;
import git.wildwind.wwhfas.registry.ModBlockEntities;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = WildWindMod.MOD_ID,dist = Dist.CLIENT)
public class ModClientHooks {

    public ModClientHooks(IEventBus modBus, ModContainer container) {
        modBus.addListener(ModClientHooks::onClientSetup);
        modBus.addListener(ModClientHooks::onRegisterRenderers);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(ModWoodType.CINDER);
            Sheets.addWoodType(ModWoodType.EMBER);
        });
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);
    }
}
