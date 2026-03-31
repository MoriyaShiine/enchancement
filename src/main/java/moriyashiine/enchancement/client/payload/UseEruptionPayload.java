/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.effect.EruptionMaceEffect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record UseEruptionPayload(int entityId) implements CustomPacketPayload {
	public static final Type<UseEruptionPayload> TYPE = new Type<>(Enchancement.id("use_eruption"));
	public static final StreamCodec<FriendlyByteBuf, UseEruptionPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UseEruptionPayload::entityId,
			UseEruptionPayload::new);

	@Override
	public Type<UseEruptionPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Player user) {
		ServerPlayNetworking.send(player, new UseEruptionPayload(user.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<UseEruptionPayload> {
		@Override
		public void receive(UseEruptionPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Player player) {
				EruptionMaceEffect.useCommon(player);
				EruptionMaceEffect.useClient(player);
			}
		}
	}
}
