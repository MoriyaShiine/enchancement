/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.enums.PayloadTarget;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record DirectionBurstPayload(int entityId, float x, float z) implements CustomPacketPayload {
	public static final Type<DirectionBurstPayload> TYPE = new Type<>(Enchancement.id("direction_burst"));
	public static final StreamCodec<FriendlyByteBuf, DirectionBurstPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, DirectionBurstPayload::entityId,
			ByteBufCodecs.FLOAT, DirectionBurstPayload::x,
			ByteBufCodecs.FLOAT, DirectionBurstPayload::z,
			DirectionBurstPayload::new);

	@Override
	public Type<DirectionBurstPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, Vec3 velocity) {
		ClientPlayNetworking.send(new DirectionBurstPayload(entity.getId(), (float) velocity.x(), (float) velocity.z()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<DirectionBurstPayload> {
		@Override
		public void receive(DirectionBurstPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.DIRECTION_BURST.maybeGet(entity).ifPresent(directionBurstComponent -> {
				if (directionBurstComponent.hasEffect() && directionBurstComponent.canUse()) {
					directionBurstComponent.use(payload.x(), payload.z());
					SLibUtils.addParticles(entity, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY, PayloadTarget.OTHERS, ParticleVelocity.ZERO);
				}
			});
		}
	}
}
