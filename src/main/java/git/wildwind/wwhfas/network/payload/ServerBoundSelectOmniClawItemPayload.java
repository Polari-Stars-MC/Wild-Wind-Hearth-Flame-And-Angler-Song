package git.wildwind.wwhfas.network.payload;

import git.wildwind.wwhfas.WildWindMod;
import git.wildwind.wwhfas.item.component.OmniClawTools;
import git.wildwind.wwhfas.registry.ModDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerBoundSelectOmniClawItemPayload(int slot, int index) implements CustomPacketPayload {
    public static final Type<ServerBoundSelectOmniClawItemPayload> TYPE = new Type<>(WildWindMod.id("select_omni_claw_item"));
    public static final StreamCodec<ByteBuf, ServerBoundSelectOmniClawItemPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerBoundSelectOmniClawItemPayload::slot,
            ByteBufCodecs.VAR_INT, ServerBoundSelectOmniClawItemPayload::index,
            ServerBoundSelectOmniClawItemPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.containerMenu.getSlot(slot).getItem();

        if (!stack.has(ModDataComponents.OMNI_CLAW_TOOLS)) return;
        OmniClawTools tools = stack.get(ModDataComponents.OMNI_CLAW_TOOLS);

        if (tools.isEmpty()) return;
        OmniClawTools.Mutable mutableTools = tools.toMutable();
        if (this.index >= mutableTools.size() || this.index < 0 || this.index == mutableTools.getSelectedToolIndex()) return;

        if (mutableTools.get(this.index).isEmpty()) return;
        mutableTools.select(this.index);

        stack.set(ModDataComponents.OMNI_CLAW_TOOLS, mutableTools.toImmutable());
    }
}
