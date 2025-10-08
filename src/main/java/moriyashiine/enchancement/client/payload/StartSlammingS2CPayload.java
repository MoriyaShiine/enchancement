/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlamComponent;
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

public record StartSlammingS2CPayload(int entityId) implements CustomPayload {
	public static final Id<StartSlammingS2CPayload> ID = new Id<>(Enchancement.id("start_slamming_s2c"));
	public static final PacketCodec<PacketByteBuf, StartSlammingS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, StartSlammingS2CPayload::entityId, StartSlammingS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity) {
		ServerPlayNetworking.send(player, new StartSlammingS2CPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StartSlammingS2CPayload> {
		@Override
		public void receive(StartSlammingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getEntityWorld().getEntityById(payload.entityId());
			if (entity instanceof PlayerEntity player) {
				SlamComponent slamComponent = ModEntityComponents.SLAM.get(player);
				slamComponent.setSlamming(true);
				slamComponent.setSlamCooldown(SlamComponent.DEFAULT_SLAM_COOLDOWN);
			}
		}
	}
}
