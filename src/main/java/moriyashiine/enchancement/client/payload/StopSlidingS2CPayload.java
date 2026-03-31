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
import net.minecraft.world.entity.player.Player;

public record StopSlidingS2CPayload(int entityId) implements CustomPacketPayload {
	public static final Type<StopSlidingS2CPayload> TYPE = new Type<>(Enchancement.id("stop_sliding_s2c"));
	public static final StreamCodec<FriendlyByteBuf, StopSlidingS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StopSlidingS2CPayload::entityId,
			StopSlidingS2CPayload::new);

	@Override
	public Type<StopSlidingS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity) {
		ServerPlayNetworking.send(player, new StopSlidingS2CPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StopSlidingS2CPayload> {
		@Override
		public void receive(StopSlidingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Player player) {
				ModEntityComponents.SLIDE.get(player).stopSliding();
			}
		}
	}
}
