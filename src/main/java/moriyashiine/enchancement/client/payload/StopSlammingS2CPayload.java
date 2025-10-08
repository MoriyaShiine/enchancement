/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record StopSlammingS2CPayload(int entityId, double posY) implements CustomPayload {
	public static final Id<StopSlammingS2CPayload> ID = new Id<>(Enchancement.id("stop_slamming_s2c"));
	public static final PacketCodec<PacketByteBuf, StopSlammingS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, StopSlammingS2CPayload::entityId, PacketCodecs.DOUBLE, StopSlammingS2CPayload::posY, StopSlammingS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, double posY) {
		ServerPlayNetworking.send(player, new StopSlammingS2CPayload(entity.getId(), posY));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StopSlammingS2CPayload> {
		@Override
		public void receive(StopSlammingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getEntityWorld().getEntityById(payload.entityId());
			if (entity instanceof PlayerEntity player) {
				ModEntityComponents.SLAM.get(player).stopSlammingClient(payload.posY());
			}
		}
	}
}
