/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BuoyPacket {
	public static final Identifier ID = Enchancement.id("buoy");

	public static void send(boolean shouldBoost) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(shouldBoost);
		ClientPlayNetworking.send(ID, buf);
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			boolean shouldBoost = buf.readBoolean();
			server.execute(() -> ModEntityComponents.BUOY.maybeGet(player).ifPresent(buoyComponent -> {
				if (buoyComponent.hasBuoy()) {
					buoyComponent.setShouldBoost(shouldBoost);
				}
			}));
		}
	}
}
