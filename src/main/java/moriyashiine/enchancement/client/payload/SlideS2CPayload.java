/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record SlideS2CPayload(int entityId, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) implements CustomPacketPayload {
	public static final Type<SlideS2CPayload> TYPE = new Type<>(Enchancement.id("slide_s2c"));
	public static final StreamCodec<FriendlyByteBuf, SlideS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SlideS2CPayload::entityId,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, SlideS2CPayload::delta,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, SlideS2CPayload::adjustedDelta,
			ByteBufCodecs.FLOAT, SlideS2CPayload::cachedYRot,
			SlideS2CPayload::new);

	@Override
	public Type<SlideS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) {
		ServerPlayNetworking.send(player, new SlideS2CPayload(entity.getId(), delta, adjustedDelta, cachedYRot));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SlideS2CPayload> {
		@Override
		public void receive(SlideS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.SLIDE.maybeGet(entity).ifPresent(slideComponent -> slideComponent.startSliding(payload.delta(), payload.adjustedDelta(), payload.cachedYRot()));
		}
	}
}
