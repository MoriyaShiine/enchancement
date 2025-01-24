/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SyncInvertedBounceStatusPayload(boolean inverted) implements CustomPayload {
	public static final Id<SyncInvertedBounceStatusPayload> ID = new Id<>(Enchancement.id("sync_inverted_bounce_status"));
	public static final PacketCodec<PacketByteBuf, SyncInvertedBounceStatusPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.BOOLEAN, SyncInvertedBounceStatusPayload::inverted,
			SyncInvertedBounceStatusPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
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
