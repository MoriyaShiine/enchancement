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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StartSlidingC2SPayload(SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) implements CustomPacketPayload {
	public static final Type<StartSlidingC2SPayload> TYPE = new Type<>(Enchancement.id("start_sliding_c2s"));
	public static final StreamCodec<FriendlyByteBuf, StartSlidingC2SPayload> CODEC = StreamCodec.composite(
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, StartSlidingC2SPayload::delta,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, StartSlidingC2SPayload::adjustedDelta,
			ByteBufCodecs.FLOAT, StartSlidingC2SPayload::cachedYRot,
			StartSlidingC2SPayload::new);

	@Override
	public Type<StartSlidingC2SPayload> type() {
		return TYPE;
	}

	public static void send(SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) {
		ClientPlayNetworking.send(new StartSlidingC2SPayload(delta, adjustedDelta, cachedYRot));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StartSlidingC2SPayload> {
		@Override
		public void receive(StartSlidingC2SPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide() && slideComponent.canSlide()) {
				slideComponent.startSliding(payload.delta(), payload.adjustedDelta(), payload.cachedYRot());
				PlayerLookup.tracking(context.player()).forEach(receiver -> StartSlidingS2CPayload.send(receiver, context.player(), payload.delta(), payload.adjustedDelta(), payload.cachedYRot()));
			}
		}
	}
}
