/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record StartSlidingS2CPayload(int entityId, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) implements CustomPacketPayload {
	public static final Type<StartSlidingS2CPayload> TYPE = new Type<>(Enchancement.id("start_sliding_s2c"));
	public static final StreamCodec<FriendlyByteBuf, StartSlidingS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StartSlidingS2CPayload::entityId,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, StartSlidingS2CPayload::delta,
			SlideComponent.SlideDeltaMovement.STREAM_CODEC, StartSlidingS2CPayload::adjustedDelta,
			ByteBufCodecs.FLOAT, StartSlidingS2CPayload::cachedYRot,
			StartSlidingS2CPayload::new);

	@Override
	public Type<StartSlidingS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, SlideComponent.SlideDeltaMovement delta, SlideComponent.SlideDeltaMovement adjustedDelta, float cachedYRot) {
		ServerPlayNetworking.send(player, new StartSlidingS2CPayload(entity.getId(), delta, adjustedDelta, cachedYRot));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StartSlidingS2CPayload> {
		@Override
		public void receive(StartSlidingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Player player) {
				ModEntityComponents.SLIDE.get(player).startSliding(payload.delta(), payload.adjustedDelta(), payload.cachedYRot());
			}
		}
	}
}
