/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.RotationMovementBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record RotationMovementBurstPayload() implements CustomPayload {
	public static final CustomPayload.Id<RotationMovementBurstPayload> ID = new Id<>(Enchancement.id("rotation_movement_burst"));
	public static final PacketCodec<PacketByteBuf, RotationMovementBurstPayload> CODEC = PacketCodec.unit(new RotationMovementBurstPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new RotationMovementBurstPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<RotationMovementBurstPayload> {
		@Override
		public void receive(RotationMovementBurstPayload payload, ServerPlayNetworking.Context context) {
			RotationMovementBurstComponent rotationMovementBurstComponent = ModEntityComponents.ROTATION_MOVEMENT_BURST.get(context.player());
			if (rotationMovementBurstComponent.hasRotationMovementBurst() && rotationMovementBurstComponent.canUse()) {
				rotationMovementBurstComponent.use();
			}
		}
	}
}
