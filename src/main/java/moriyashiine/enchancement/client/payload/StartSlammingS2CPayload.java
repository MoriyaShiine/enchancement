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

public record StartSlammingS2CPayload(int entityId) implements CustomPacketPayload {
	public static final Type<StartSlammingS2CPayload> TYPE = new Type<>(Enchancement.id("start_slamming_s2c"));
	public static final StreamCodec<FriendlyByteBuf, StartSlammingS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StartSlammingS2CPayload::entityId,
			StartSlammingS2CPayload::new);

	@Override
	public Type<StartSlammingS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity) {
		ServerPlayNetworking.send(player, new StartSlammingS2CPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StartSlammingS2CPayload> {
		@Override
		public void receive(StartSlammingS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Player player) {
				ModEntityComponents.SLAM.get(player).setSlamming(true);
			}
		}
	}
}
