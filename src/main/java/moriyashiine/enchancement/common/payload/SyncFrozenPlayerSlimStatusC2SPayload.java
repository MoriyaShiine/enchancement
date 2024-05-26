/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SyncFrozenPlayerSlimStatusC2SPayload(UUID uuid, boolean slim) implements CustomPayload {
	public static final CustomPayload.Id<SyncFrozenPlayerSlimStatusC2SPayload> ID = CustomPayload.id(Enchancement.id("sync_frozen_player_slim_status_cs2").toString());
	public static final PacketCodec<PacketByteBuf, SyncFrozenPlayerSlimStatusC2SPayload> CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, SyncFrozenPlayerSlimStatusC2SPayload::uuid, PacketCodecs.BOOL, SyncFrozenPlayerSlimStatusC2SPayload::slim, SyncFrozenPlayerSlimStatusC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(UUID uuid, boolean slim) {
		ClientPlayNetworking.send(new SyncFrozenPlayerSlimStatusC2SPayload(uuid, slim));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncFrozenPlayerSlimStatusC2SPayload> {
		@Override
		public void receive(SyncFrozenPlayerSlimStatusC2SPayload payload, ServerPlayNetworking.Context context) {
			FrozenPlayerEntity.SLIM_STATUSES.put(payload.uuid(), payload.slim());
		}
	}
}
