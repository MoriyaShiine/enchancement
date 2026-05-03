/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.SlideS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public record SlideC2SPayload(int entityId, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) implements CustomPacketPayload {
	public static final Type<SlideC2SPayload> TYPE = new Type<>(Enchancement.id("slide_c2s"));
	public static final StreamCodec<FriendlyByteBuf, SlideC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SlideC2SPayload::entityId,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, SlideC2SPayload::delta,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, SlideC2SPayload::adjustedDelta,
			ByteBufCodecs.FLOAT, SlideC2SPayload::cachedYRot,
			SlideC2SPayload::new);

	@Override
	public Type<SlideC2SPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) {
		ClientPlayNetworking.send(new SlideC2SPayload(entity.getId(), delta, adjustedDelta, cachedYRot));
	}

	public static void sendStop(Entity entity) {
		send(entity, SlideComponent.SlideDeltaMovement.ZERO, SlideComponent.SlideDeltaMovement.ZERO, 0);
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SlideC2SPayload> {
		@Override
		public void receive(SlideC2SPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.SLIDE.maybeGet(entity).ifPresent(slideComponent -> {
				if (slideComponent.hasSlide()) {
					if (payload.delta().equals(SlideComponent.SlideDeltaMovement.ZERO) || slideComponent.canSlide()) {
						slideComponent.startSliding(payload.delta(), payload.adjustedDelta(), payload.cachedYRot());
					}
					PlayerLookup.tracking(entity).forEach(receiver -> SlideS2CPayload.send(receiver, entity, payload.delta(), payload.adjustedDelta(), payload.cachedYRot()));
				}
			});
		}
	}
}
