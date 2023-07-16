/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.StopBrimstoneSoundsS2CPacket;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class StopBrimstoneSoundsC2SPacket {
	public static final Identifier ID = Enchancement.id("stop_brimstone_sounds_c2s");

	public static void send(UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		ClientPlayNetworking.send(ID, buf);
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			UUID uuid = buf.readUuid();
			server.execute(() -> PlayerLookup.tracking(player).forEach(foundPlayer -> StopBrimstoneSoundsS2CPacket.send(foundPlayer, uuid)));
		}
	}
}
