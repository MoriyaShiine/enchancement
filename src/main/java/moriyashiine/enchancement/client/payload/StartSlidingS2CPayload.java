/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
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

public record StartSlidingS2CPayload(int entityId, float velocityX, float velocityZ, float adjustedVelocityX,
									 float adjustedVelocityZ, float cachedYaw) implements CustomPayload {
	public static final Id<StartSlidingS2CPayload> ID = new Id<>(Enchancement.id("start_sliding_s2c"));
	public static final PacketCodec<PacketByteBuf, StartSlidingS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, StartSlidingS2CPayload::entityId, PacketCodecs.FLOAT, StartSlidingS2CPayload::velocityX,
			PacketCodecs.FLOAT, StartSlidingS2CPayload::velocityZ,
			PacketCodecs.FLOAT, StartSlidingS2CPayload::adjustedVelocityX,
			PacketCodecs.FLOAT, StartSlidingS2CPayload::adjustedVelocityZ,
			PacketCodecs.FLOAT, StartSlidingS2CPayload::cachedYaw,
			StartSlidingS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, SlideComponent.SlideVelocity velocity, SlideComponent.SlideVelocity adjustedVelocity, float cachedYaw) {
		ServerPlayNetworking.send(player, new StartSlidingS2CPayload(entity.getId(), velocity.x(), velocity.z(), adjustedVelocity.x(), adjustedVelocity.z(), cachedYaw));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StartSlidingS2CPayload> {
		@Override
		public void receive(StartSlidingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof PlayerEntity player) {
				ModEntityComponents.SLIDE.get(player).startSliding(new SlideComponent.SlideVelocity(payload.velocityX(), payload.velocityZ()), new SlideComponent.SlideVelocity(payload.adjustedVelocityX(), payload.adjustedVelocityZ()), payload.cachedYaw());
			}
		}
	}
}
