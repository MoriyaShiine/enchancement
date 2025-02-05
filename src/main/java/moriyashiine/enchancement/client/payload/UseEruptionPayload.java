/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record UseEruptionPayload(int entityId) implements CustomPayload {
	public static final Id<UseEruptionPayload> ID = new Id<>(Enchancement.id("use_eruption"));
	public static final PacketCodec<PacketByteBuf, UseEruptionPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, UseEruptionPayload::entityId,
			UseEruptionPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity) {
		ServerPlayNetworking.send(player, new UseEruptionPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<UseEruptionPayload> {
		@Override
		public void receive(UseEruptionPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				ModEntityComponents.ERUPTION.maybeGet(entity).ifPresent(eruptionComponent -> {
					eruptionComponent.useCommon();
					eruptionComponent.useClient();
				});
			}
		}
	}
}
