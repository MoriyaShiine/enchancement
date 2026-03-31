/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.payload.SyncFrozenPlayerSlimStatusC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.PlayerModelType;

import java.util.UUID;

public record SyncFrozenPlayerSlimStatusS2CPayload(UUID uuid) implements CustomPacketPayload {
	public static final Type<SyncFrozenPlayerSlimStatusS2CPayload> TYPE = new Type<>(Enchancement.id("sync_frozen_player_slim_status_s2c"));
	public static final StreamCodec<FriendlyByteBuf, SyncFrozenPlayerSlimStatusS2CPayload> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, SyncFrozenPlayerSlimStatusS2CPayload::uuid,
			SyncFrozenPlayerSlimStatusS2CPayload::new);

	@Override
	public Type<SyncFrozenPlayerSlimStatusS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, UUID uuid) {
		ServerPlayNetworking.send(player, new SyncFrozenPlayerSlimStatusS2CPayload(uuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncFrozenPlayerSlimStatusS2CPayload> {
		@Override
		public void receive(SyncFrozenPlayerSlimStatusS2CPayload payload, ClientPlayNetworking.Context context) {
			SyncFrozenPlayerSlimStatusC2SPayload.send(payload.uuid(), context.player().getSkin().model() == PlayerModelType.SLIM);
		}
	}
}
