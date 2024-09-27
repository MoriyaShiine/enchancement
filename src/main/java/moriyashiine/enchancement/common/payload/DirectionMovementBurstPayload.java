/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.AddStrafeParticlesPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DirectionMovementBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record DirectionMovementBurstPayload(float velocityX, float velocityZ) implements CustomPayload {
	public static final CustomPayload.Id<DirectionMovementBurstPayload> ID = new Id<>(Enchancement.id("direction_movement_burst"));
	public static final PacketCodec<PacketByteBuf, DirectionMovementBurstPayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, DirectionMovementBurstPayload::velocityX, PacketCodecs.FLOAT, DirectionMovementBurstPayload::velocityZ, DirectionMovementBurstPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new DirectionMovementBurstPayload((float) velocity.getX(), (float) velocity.getZ()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<DirectionMovementBurstPayload> {
		@Override
		public void receive(DirectionMovementBurstPayload payload, ServerPlayNetworking.Context context) {
			DirectionMovementBurstComponent directionMovementBurstComponent = ModEntityComponents.DIRECTION_MOVEMENT_BURST.get(context.player());
			if (directionMovementBurstComponent.hasDirectionMovementBurst() && directionMovementBurstComponent.canUse()) {
				directionMovementBurstComponent.use(payload.velocityX(), payload.velocityZ());
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> AddStrafeParticlesPayload.send(foundPlayer, context.player().getId()));
			}
		}
	}
}
