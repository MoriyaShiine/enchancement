package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddStrafeParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DashPacket {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "dash");

	public static void send() {
		ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> ModEntityComponents.DASH.maybeGet(player).ifPresent(dashComponent -> {
			if (dashComponent.hasDash()) {
				EnchancementUtil.PACKET_IMMUNITIES.put(player, 20);
				DashComponent.handle(player, dashComponent);
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddStrafeParticlesPacket.send(foundPlayer, player.getId()));
			}
		}));
	}
}
