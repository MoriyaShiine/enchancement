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
import net.minecraft.network.packet.CustomPayload;

public record StopSlidingPayload() implements CustomPayload {
	public static final CustomPayload.Id<StopSlidingPayload> ID = new Id<>(Enchancement.id("stop_sliding"));
	public static final PacketCodec<PacketByteBuf, StopSlidingPayload> CODEC = PacketCodec.unit(new StopSlidingPayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new StopSlidingPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StopSlidingPayload> {
		@Override
		public void receive(StopSlidingPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide()) {
				slideComponent.stopSliding();
			}
		}
	}
}
