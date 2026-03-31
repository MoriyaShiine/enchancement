/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.internal.SyncDeltaMovementsEvent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record SyncDeltaMovementPayload(Vec3 delta) implements CustomPacketPayload {
	public static final Type<SyncDeltaMovementPayload> TYPE = new Type<>(Enchancement.id("sync_delta_movement"));
	public static final StreamCodec<FriendlyByteBuf, SyncDeltaMovementPayload> CODEC = StreamCodec.composite(
			Vec3.STREAM_CODEC, SyncDeltaMovementPayload::delta,
			SyncDeltaMovementPayload::new
	);

	@Override
	public Type<SyncDeltaMovementPayload> type() {
		return TYPE;
	}

	public static void send(Vec3 delta) {
		ClientPlayNetworking.send(new SyncDeltaMovementPayload(delta));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncDeltaMovementPayload> {
		@Override
		public void receive(SyncDeltaMovementPayload payload, ServerPlayNetworking.Context context) {
			SyncDeltaMovementsEvent.DELTAS.put(context.player().getUUID(), payload.delta());
		}
	}
}
