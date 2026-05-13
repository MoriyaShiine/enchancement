/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.EMeterS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.EMeterComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record EMeterC2SPayload(int entityId, boolean shouldFloat) implements CustomPacketPayload {
	public static final Type<EMeterC2SPayload> TYPE = new Type<>(Enchancement.id("e_meter_c2s"));
	public static final StreamCodec<FriendlyByteBuf, EMeterC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, EMeterC2SPayload::entityId,
			ByteBufCodecs.BOOL, EMeterC2SPayload::shouldFloat,
			EMeterC2SPayload::new);

	@Override
	public Type<EMeterC2SPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, boolean shouldFloat) {
		ClientPlayNetworking.send(new EMeterC2SPayload(entity.getId(), shouldFloat));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<EMeterC2SPayload> {
		@Override
		public void receive(EMeterC2SPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				EMeterComponent eMeterComponent = ModEntityComponents.E_METER.get(entity);
				if (eMeterComponent.hasEMeter() && eMeterComponent.canFloat()) {
					eMeterComponent.setShouldFloat(payload.shouldFloat());
					PlayerLookup.tracking(entity).forEach(receiver -> EMeterS2CPayload.send(receiver, entity, payload.shouldFloat()));
				}
			}
		}
	}
}
