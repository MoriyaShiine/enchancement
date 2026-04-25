/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StopSlidingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StopSlidingC2SPayload() implements CustomPacketPayload {
	public static final Type<StopSlidingC2SPayload> TYPE = new Type<>(Enchancement.id("stop_sliding_c2s"));
	public static final StreamCodec<FriendlyByteBuf, StopSlidingC2SPayload> CODEC = StreamCodec.unit(new StopSlidingC2SPayload());

	@Override
	public Type<StopSlidingC2SPayload> type() {
		return TYPE;
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
				PlayerLookup.tracking(context.player()).forEach(receiver -> StopSlidingS2CPayload.send(receiver, context.player()));
			}
		}
	}
}
