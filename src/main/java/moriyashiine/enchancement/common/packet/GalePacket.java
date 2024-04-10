/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddGaleParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GalePacket {
	public static final Identifier ID = Enchancement.id("gale");

	public static void send() {
		ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			server.execute(() -> {
				GaleComponent galeComponent = ModEntityComponents.GALE.get(player);
				if (galeComponent.hasGale() && galeComponent.canUse()) {
					galeComponent.use();
					PlayerLookup.tracking(player).forEach(foundPlayer -> AddGaleParticlesPacket.send(foundPlayer, player.getId()));
				}
			});
		}
	}
}
