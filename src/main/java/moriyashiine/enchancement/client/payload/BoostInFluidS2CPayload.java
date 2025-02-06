/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record BoostInFluidS2CPayload(int entityId, boolean shouldBoost) implements CustomPayload {
	public static final Id<BoostInFluidS2CPayload> ID = new Id<>(Enchancement.id("boost_in_fluid_s2c"));
	public static final PacketCodec<PacketByteBuf, BoostInFluidS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, BoostInFluidS2CPayload::entityId, PacketCodecs.BOOLEAN, BoostInFluidS2CPayload::shouldBoost, BoostInFluidS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, boolean shouldBoost) {
		ServerPlayNetworking.send(player, new BoostInFluidS2CPayload(entity.getId(), shouldBoost));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<BoostInFluidS2CPayload> {
		@Override
		public void receive(BoostInFluidS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof LivingEntity) {
				ModEntityComponents.BOOST_IN_FLUID.get(entity).setShouldBoost(payload.shouldBoost());
			}
		}
	}
}
