/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.AddAirJumpParticlesPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record AirJumpPayload() implements CustomPayload {
	public static final CustomPayload.Id<AirJumpPayload> ID = new Id<>(Enchancement.id("air_jump"));
	public static final PacketCodec<PacketByteBuf, AirJumpPayload> CODEC = PacketCodec.unit(new AirJumpPayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new AirJumpPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<AirJumpPayload> {
		@Override
		public void receive(AirJumpPayload payload, ServerPlayNetworking.Context context) {
			AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.get(context.player());
			if (airJumpComponent.hasAirJump() && airJumpComponent.canUse()) {
				airJumpComponent.use();
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> AddAirJumpParticlesPayload.send(foundPlayer, context.player().getId()));
			}
		}
	}
}
