/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record StopSlammingS2CPayload(int entityId, double posY) implements CustomPacketPayload {
	public static final Type<StopSlammingS2CPayload> TYPE = new Type<>(Enchancement.id("stop_slamming_s2c"));
	public static final StreamCodec<FriendlyByteBuf, StopSlammingS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StopSlammingS2CPayload::entityId,
			ByteBufCodecs.DOUBLE, StopSlammingS2CPayload::posY,
			StopSlammingS2CPayload::new);

	@Override
	public Type<StopSlammingS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, double posY) {
		ServerPlayNetworking.send(player, new StopSlammingS2CPayload(entity.getId(), posY));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StopSlammingS2CPayload> {
		@Override
		public void receive(StopSlammingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.SLAM.maybeGet(entity).ifPresent(slamComponent -> slamComponent.stopSlammingClient(payload.posY()));
		}
	}
}
