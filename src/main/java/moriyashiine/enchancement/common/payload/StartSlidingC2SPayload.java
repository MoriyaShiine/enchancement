/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StartSlidingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StartSlidingC2SPayload(float velocityX, float velocityZ, float adjustedVelocityX,
									 float adjustedVelocityZ, float cachedYaw) implements CustomPayload {
	public static final CustomPayload.Id<StartSlidingC2SPayload> ID = new Id<>(Enchancement.id("start_sliding_c2s"));
	public static final PacketCodec<PacketByteBuf, StartSlidingC2SPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, StartSlidingC2SPayload::velocityX,
			PacketCodecs.FLOAT, StartSlidingC2SPayload::velocityZ,
			PacketCodecs.FLOAT, StartSlidingC2SPayload::adjustedVelocityX,
			PacketCodecs.FLOAT, StartSlidingC2SPayload::adjustedVelocityZ,
			PacketCodecs.FLOAT, StartSlidingC2SPayload::cachedYaw,
			StartSlidingC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(SlideComponent.SlideVelocity velocity, SlideComponent.SlideVelocity adjustedVelocity, float cachedYaw) {
		ClientPlayNetworking.send(new StartSlidingC2SPayload(velocity.x(), velocity.z(), adjustedVelocity.x(), adjustedVelocity.z(), cachedYaw));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StartSlidingC2SPayload> {
		@Override
		public void receive(StartSlidingC2SPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide() && slideComponent.canSlide()) {
				SlideComponent.SlideVelocity velocity = new SlideComponent.SlideVelocity(payload.velocityX(), payload.velocityZ());
				SlideComponent.SlideVelocity adjustedVelocity = new SlideComponent.SlideVelocity(payload.adjustedVelocityX(), payload.adjustedVelocityZ());
				slideComponent.startSliding(velocity, adjustedVelocity, payload.cachedYaw());
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> StartSlidingS2CPayload.send(foundPlayer, context.player(), velocity, adjustedVelocity, payload.cachedYaw()));
			}
		}
	}
}
