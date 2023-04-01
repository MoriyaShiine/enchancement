/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.packet.SyncFrozenPlayerSlimStatusC2S;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SyncFrozenPlayerSlimStatusS2C {
	public static final Identifier ID = Enchancement.id("sync_frozen_player_slim_status_s2c");

	public static void send(ServerPlayerEntity player, UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUuid();
		client.execute(() -> {
			if (client.player != null) {
				SyncFrozenPlayerSlimStatusC2S.send(uuid, client.player.getModel().equals("slim"));
			}
		});
	}
}
