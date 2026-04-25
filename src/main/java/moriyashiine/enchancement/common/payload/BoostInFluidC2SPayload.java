/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.BoostInFluidS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BoostInFluidComponent;
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

public record BoostInFluidC2SPayload(int entityId, boolean shouldBoost) implements CustomPacketPayload {
	public static final Type<BoostInFluidC2SPayload> TYPE = new Type<>(Enchancement.id("boost_in_fluid_c2s"));
	public static final StreamCodec<FriendlyByteBuf, BoostInFluidC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, BoostInFluidC2SPayload::entityId,
			ByteBufCodecs.BOOL, BoostInFluidC2SPayload::shouldBoost,
			BoostInFluidC2SPayload::new);

	@Override
	public Type<BoostInFluidC2SPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, boolean shouldBoost) {
		ClientPlayNetworking.send(new BoostInFluidC2SPayload(entity.getId(), shouldBoost));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BoostInFluidC2SPayload> {
		@Override
		public void receive(BoostInFluidC2SPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				BoostInFluidComponent boostInFluidComponent = ModEntityComponents.BOOST_IN_FLUID.get(entity);
				if (boostInFluidComponent.hasBoost() && boostInFluidComponent.canUse(true)) {
					boostInFluidComponent.setShouldBoost(payload.shouldBoost());
					PlayerLookup.tracking(entity).forEach(receiver -> BoostInFluidS2CPayload.send(receiver, entity, payload.shouldBoost()));
				}
			}
		}
	}
}
