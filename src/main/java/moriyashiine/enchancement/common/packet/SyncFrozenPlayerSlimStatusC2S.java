/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SyncFrozenPlayerSlimStatusC2S {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "sync_frozen_player_slim_status_cs2");

	public static void send(UUID uuid, boolean slim) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		buf.writeBoolean(slim);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUuid();
		boolean slim = buf.readBoolean();
		server.execute(() -> FrozenPlayerEntity.SLIM_STATUSES.put(uuid, slim));
	}
}
