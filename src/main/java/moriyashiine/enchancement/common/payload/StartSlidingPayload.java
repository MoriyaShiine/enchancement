/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StartSlidingPayload(float velocityX, float velocityZ, float adjustedVelocityX,
								  float adjustedVelocityZ, float cachedYaw) implements CustomPayload {
	public static final CustomPayload.Id<StartSlidingPayload> ID = new Id<>(Enchancement.id("start_sliding"));
	public static final PacketCodec<PacketByteBuf, StartSlidingPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, StartSlidingPayload::velocityX,
			PacketCodecs.FLOAT, StartSlidingPayload::velocityZ,
			PacketCodecs.FLOAT, StartSlidingPayload::adjustedVelocityX,
			PacketCodecs.FLOAT, StartSlidingPayload::adjustedVelocityZ,
			PacketCodecs.FLOAT, StartSlidingPayload::cachedYaw,
			StartSlidingPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(SlideComponent.SlideVelocity velocity, SlideComponent.SlideVelocity adjustedVelocity, float cachedYaw) {
		ClientPlayNetworking.send(new StartSlidingPayload(velocity.x(), velocity.z(), adjustedVelocity.x(), adjustedVelocity.z(), cachedYaw));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StartSlidingPayload> {
		@Override
		public void receive(StartSlidingPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide() && slideComponent.canSlide()) {
				slideComponent.startSliding(new SlideComponent.SlideVelocity(payload.velocityX(), payload.velocityZ()), new SlideComponent.SlideVelocity(payload.adjustedVelocityX(), payload.adjustedVelocityZ()), payload.cachedYaw());
			}
		}
	}
}
