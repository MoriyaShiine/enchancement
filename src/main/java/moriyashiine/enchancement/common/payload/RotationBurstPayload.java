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

public record RotationBurstPayload(int entityId) implements CustomPacketPayload {
	public static final Type<RotationBurstPayload> TYPE = new Type<>(Enchancement.id("rotation_burst"));
	public static final StreamCodec<FriendlyByteBuf, RotationBurstPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, RotationBurstPayload::entityId,
			RotationBurstPayload::new);

	@Override
	public Type<RotationBurstPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity) {
		ClientPlayNetworking.send(new RotationBurstPayload(entity.getId()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<RotationBurstPayload> {
		@Override
		public void receive(RotationBurstPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.ROTATION_BURST.maybeGet(entity).ifPresent(rotationBurstComponent -> {
				if (rotationBurstComponent.hasEffect() && rotationBurstComponent.canUse()) {
					rotationBurstComponent.use();
					SLibUtils.addParticles(entity, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY, PayloadTarget.OTHERS, ParticleVelocity.ZERO);
				}
			});
		}
	}
}
