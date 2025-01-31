/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StopSlidingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record StopSlidingC2SPayload() implements CustomPayload {
	public static final CustomPayload.Id<StopSlidingC2SPayload> ID = new Id<>(Enchancement.id("stop_sliding_c2s"));
	public static final PacketCodec<PacketByteBuf, StopSlidingC2SPayload> CODEC = PacketCodec.unit(new StopSlidingC2SPayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new StopSlidingC2SPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StopSlidingC2SPayload> {
		@Override
		public void receive(StopSlidingC2SPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide()) {
				slideComponent.stopSliding();
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> StopSlidingS2CPayload.send(foundPlayer, context.player()));
			}
		}
	}
}
