/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.enums.PayloadTarget;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RotationBurstPayload() implements CustomPacketPayload {
	public static final Type<RotationBurstPayload> TYPE = new Type<>(Enchancement.id("rotation_burst"));
	public static final StreamCodec<FriendlyByteBuf, RotationBurstPayload> CODEC = StreamCodec.unit(new RotationBurstPayload());

	@Override
	public Type<RotationBurstPayload> type() {
		return TYPE;
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
				SLibUtils.addParticles(context.player(), ParticleTypes.CLOUD, 8, ParticleAnchor.BODY, PayloadTarget.OTHERS, ParticleVelocity.ZERO);
			}
		}
	}
}
