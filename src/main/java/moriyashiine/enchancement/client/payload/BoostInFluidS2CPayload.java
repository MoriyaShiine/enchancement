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
import net.minecraft.world.entity.LivingEntity;

public record BoostInFluidS2CPayload(int entityId, boolean shouldBoost) implements CustomPacketPayload {
	public static final Type<BoostInFluidS2CPayload> TYPE = new Type<>(Enchancement.id("boost_in_fluid_s2c"));
	public static final StreamCodec<FriendlyByteBuf, BoostInFluidS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, BoostInFluidS2CPayload::entityId,
			ByteBufCodecs.BOOL, BoostInFluidS2CPayload::shouldBoost,
			BoostInFluidS2CPayload::new);

	@Override
	public Type<BoostInFluidS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, boolean shouldBoost) {
		ServerPlayNetworking.send(player, new BoostInFluidS2CPayload(entity.getId(), shouldBoost));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<BoostInFluidS2CPayload> {
		@Override
		public void receive(BoostInFluidS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				ModEntityComponents.BOOST_IN_FLUID.get(entity).setShouldBoost(payload.shouldBoost());
			}
		}
	}
}
