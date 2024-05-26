/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record ResetFrozenTicksPayload() implements CustomPayload {
	public static final CustomPayload.Id<ResetFrozenTicksPayload> ID = CustomPayload.id(Enchancement.id("reset_frozen_ticks").toString());
	public static final PacketCodec<PacketByteBuf, ResetFrozenTicksPayload> CODEC = PacketCodec.unit(new ResetFrozenTicksPayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, new ResetFrozenTicksPayload());
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<ResetFrozenTicksPayload> {
		@Override
		public void receive(ResetFrozenTicksPayload payload, ClientPlayNetworking.Context context) {
			context.player().setFrozenTicks(0);
		}
	}
}
