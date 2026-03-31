/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record SyncFrozenPlayerSlimStatusC2SPayload(UUID uuid, boolean slim) implements CustomPacketPayload {
	public static final Type<SyncFrozenPlayerSlimStatusC2SPayload> TYPE = new Type<>(Enchancement.id("sync_frozen_player_slim_status_cs2"));
	public static final StreamCodec<FriendlyByteBuf, SyncFrozenPlayerSlimStatusC2SPayload> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, SyncFrozenPlayerSlimStatusC2SPayload::uuid,
			ByteBufCodecs.BOOL, SyncFrozenPlayerSlimStatusC2SPayload::slim,
			SyncFrozenPlayerSlimStatusC2SPayload::new);

	@Override
	public Type<SyncFrozenPlayerSlimStatusC2SPayload> type() {
		return TYPE;
	}

	public static void send(UUID uuid, boolean slim) {
		ClientPlayNetworking.send(new SyncFrozenPlayerSlimStatusC2SPayload(uuid, slim));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncFrozenPlayerSlimStatusC2SPayload> {
		@Override
		public void receive(SyncFrozenPlayerSlimStatusC2SPayload payload, ServerPlayNetworking.Context context) {
			FrozenPlayer.SLIM_STATUSES.put(payload.uuid(), payload.slim());
		}
	}
}
