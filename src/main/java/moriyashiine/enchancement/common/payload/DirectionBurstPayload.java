/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DirectionBurstComponent;
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

public record DirectionBurstPayload(int entityId, Vec3 delta) implements CustomPacketPayload {
	public static final Type<DirectionBurstPayload> TYPE = new Type<>(Enchancement.id("direction_burst"));
	public static final StreamCodec<FriendlyByteBuf, DirectionBurstPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, DirectionBurstPayload::entityId,
			Vec3.STREAM_CODEC, DirectionBurstPayload::delta,
			DirectionBurstPayload::new);

	@Override
	public Type<DirectionBurstPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, Vec3 delta) {
		ClientPlayNetworking.send(new DirectionBurstPayload(entity.getId(), delta));
	}

	public static void use(Entity entity, Vec3 delta, DirectionBurstComponent directionBurstComponent) {
		directionBurstComponent.use(delta);
		SLibUtils.addParticles(entity, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY, PayloadTarget.OTHERS, ParticleVelocity.ZERO);

	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<DirectionBurstPayload> {
		@Override
		public void receive(DirectionBurstPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.DIRECTION_BURST.maybeGet(entity).ifPresent(directionBurstComponent -> {
				if (directionBurstComponent.hasEffect() && directionBurstComponent.canUse()) {
					use(entity, payload.delta(), directionBurstComponent);
				}
			});
		}
	}
}
