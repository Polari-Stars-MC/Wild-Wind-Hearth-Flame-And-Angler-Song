package git.wildwind.wwhfas.network;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.network.payload.ServerBoundSelectOmniClawItemPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 模组网络负载注册入口喵~
 */
@EventBusSubscriber(modid = WildWindMod.MOD_ID)
public class WildWindNetwork {

	@SubscribeEvent
	static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.commonToServer(
				ServerBoundSelectOmniClawItemPayload.TYPE,
				ServerBoundSelectOmniClawItemPayload.STREAM_CODEC,
				ServerBoundSelectOmniClawItemPayload::handle
		);
	}

	private WildWindNetwork() {
	}
}
