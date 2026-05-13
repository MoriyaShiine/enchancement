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

public record EMeterS2CPayload(int entityId, boolean shouldFloat) implements CustomPacketPayload {
	public static final Type<EMeterS2CPayload> TYPE = new Type<>(Enchancement.id("e_meter_s2c"));
	public static final StreamCodec<FriendlyByteBuf, EMeterS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, EMeterS2CPayload::entityId,
			ByteBufCodecs.BOOL, EMeterS2CPayload::shouldFloat,
			EMeterS2CPayload::new);

	@Override
	public Type<EMeterS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, boolean shouldFloat) {
		ServerPlayNetworking.send(player, new EMeterS2CPayload(entity.getId(), shouldFloat));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<EMeterS2CPayload> {
		@Override
		public void receive(EMeterS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				ModEntityComponents.E_METER.get(entity).setShouldFloat(payload.shouldFloat());
			}
		}
	}
}
