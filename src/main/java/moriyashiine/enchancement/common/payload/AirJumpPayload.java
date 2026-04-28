/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.AirJumpComponent;
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

public record AirJumpPayload() implements CustomPacketPayload {
	public static final Type<AirJumpPayload> TYPE = new Type<>(Enchancement.id("air_jump"));
	public static final StreamCodec<FriendlyByteBuf, AirJumpPayload> CODEC = StreamCodec.unit(new AirJumpPayload());

	@Override
	public Type<AirJumpPayload> type() {
		return TYPE;
	}

	public static void send() {
		ClientPlayNetworking.send(new AirJumpPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<AirJumpPayload> {
		@Override
		public void receive(AirJumpPayload payload, ServerPlayNetworking.Context context) {
			AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.get(context.player());
			if (airJumpComponent.hasEffect() && airJumpComponent.canUse()) {
				airJumpComponent.use();
				SLibUtils.addParticles(context.player(), ParticleTypes.CLOUD, 8, ParticleAnchor.BASE, PayloadTarget.OTHERS, ParticleVelocity.ZERO);
			}
		}
	}
}
// Ryan was here
