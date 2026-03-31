/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncInvertedBounceStatusPayload(boolean inverted) implements CustomPacketPayload {
	public static final Type<SyncInvertedBounceStatusPayload> TYPE = new Type<>(Enchancement.id("sync_inverted_bounce_status"));
	public static final StreamCodec<FriendlyByteBuf, SyncInvertedBounceStatusPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SyncInvertedBounceStatusPayload::inverted,
			SyncInvertedBounceStatusPayload::new
	);

	@Override
	public Type<SyncInvertedBounceStatusPayload> type() {
		return TYPE;
	}

	public static void send(boolean inverted) {
		ClientPlayNetworking.send(new SyncInvertedBounceStatusPayload(inverted));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncInvertedBounceStatusPayload> {
		@Override
		public void receive(SyncInvertedBounceStatusPayload payload, ServerPlayNetworking.Context context) {
			ModEntityComponents.BOUNCE.get(context.player()).setInvertedBounce(payload.inverted());
		}
	}
}
