/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.AddMovementBurstParticlesPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record RotationBurstPayload() implements CustomPayload {
	public static final CustomPayload.Id<RotationBurstPayload> ID = new Id<>(Enchancement.id("rotation_burst"));
	public static final PacketCodec<PacketByteBuf, RotationBurstPayload> CODEC = PacketCodec.unit(new RotationBurstPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new RotationBurstPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<RotationBurstPayload> {
		@Override
		public void receive(RotationBurstPayload payload, ServerPlayNetworking.Context context) {
			RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.get(context.player());
			if (rotationBurstComponent.hasRotationBurst() && rotationBurstComponent.canUse()) {
				rotationBurstComponent.use();
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> AddMovementBurstParticlesPayload.send(foundPlayer, context.player()));
			}
		}
	}
}
