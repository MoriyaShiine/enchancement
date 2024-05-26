/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.payload.SyncFrozenPlayerSlimStatusC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SyncFrozenPlayerSlimStatusS2CPayload(UUID uuid) implements CustomPayload {
	public static final CustomPayload.Id<SyncFrozenPlayerSlimStatusS2CPayload> ID = CustomPayload.id(Enchancement.id("sync_frozen_player_slim_status_s2c").toString());
	public static final PacketCodec<PacketByteBuf, SyncFrozenPlayerSlimStatusS2CPayload> CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, SyncFrozenPlayerSlimStatusS2CPayload::uuid, SyncFrozenPlayerSlimStatusS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, UUID uuid) {
		ServerPlayNetworking.send(player, new SyncFrozenPlayerSlimStatusS2CPayload(uuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncFrozenPlayerSlimStatusS2CPayload> {
		@Override
		public void receive(SyncFrozenPlayerSlimStatusS2CPayload payload, ClientPlayNetworking.Context context) {
			SyncFrozenPlayerSlimStatusC2SPayload.send(payload.uuid(), context.player().getSkinTextures().model() == SkinTextures.Model.SLIM);
		}
	}
}
