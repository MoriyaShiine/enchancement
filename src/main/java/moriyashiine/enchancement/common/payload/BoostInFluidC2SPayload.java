/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.BoostInFluidS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.BoostInFluidComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BoostInFluidC2SPayload(int entityId, boolean shouldBoost) implements CustomPayload {
	public static final CustomPayload.Id<BoostInFluidC2SPayload> ID = new Id<>(Enchancement.id("boost_in_fluid_c2s"));
	public static final PacketCodec<PacketByteBuf, BoostInFluidC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, BoostInFluidC2SPayload::entityId, PacketCodecs.BOOLEAN, BoostInFluidC2SPayload::shouldBoost, BoostInFluidC2SPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Entity entity, boolean shouldBoost) {
		ClientPlayNetworking.send(new BoostInFluidC2SPayload(entity.getId(), shouldBoost));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BoostInFluidC2SPayload> {
		@Override
		public void receive(BoostInFluidC2SPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof LivingEntity) {
				BoostInFluidComponent boostInFluidComponent = ModEntityComponents.BOOST_IN_FLUID.get(entity);
				if (boostInFluidComponent.hasBoost() && boostInFluidComponent.canUse(true)) {
					boostInFluidComponent.setShouldBoost(payload.shouldBoost());
					PlayerLookup.tracking(entity).forEach(foundPlayer -> BoostInFluidS2CPayload.send(foundPlayer, entity, payload.shouldBoost()));
				}
			}
		}
	}
}
