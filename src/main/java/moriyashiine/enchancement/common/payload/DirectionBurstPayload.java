/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.PacketTarget;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public record DirectionBurstPayload(float velocityX, float velocityZ) implements CustomPayload {
	public static final CustomPayload.Id<DirectionBurstPayload> ID = new Id<>(Enchancement.id("direction_burst"));
	public static final PacketCodec<PacketByteBuf, DirectionBurstPayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, DirectionBurstPayload::velocityX, PacketCodecs.FLOAT, DirectionBurstPayload::velocityZ, DirectionBurstPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new DirectionBurstPayload((float) velocity.getX(), (float) velocity.getZ()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<DirectionBurstPayload> {
		@Override
		public void receive(DirectionBurstPayload payload, ServerPlayNetworking.Context context) {
			DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.get(context.player());
			if (directionBurstComponent.hasDirectionBurst() && directionBurstComponent.canUse()) {
				directionBurstComponent.use(payload.velocityX(), payload.velocityZ());
				SLibUtils.addParticles(context.player(), ParticleTypes.CLOUD, 8, ParticleAnchor.BODY, PacketTarget.OTHERS, ParticleVelocity.ZERO);
			}
		}
	}
}
