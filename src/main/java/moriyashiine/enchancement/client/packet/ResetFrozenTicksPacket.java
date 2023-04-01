/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ResetFrozenTicksPacket {
	public static final Identifier ID = Enchancement.id("reset_frozen_ticks");

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> {
			if (client.player != null) {
				client.player.setFrozenTicks(0);
			}
		});
	}
}
