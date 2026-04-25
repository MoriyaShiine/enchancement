/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StopSlammingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StopSlammingC2SPayload(double posY) implements CustomPacketPayload {
	public static final Type<StopSlammingC2SPayload> TYPE = new Type<>(Enchancement.id("stop_slamming_c2s"));
	public static final StreamCodec<FriendlyByteBuf, StopSlammingC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, StopSlammingC2SPayload::posY,
			StopSlammingC2SPayload::new);

	@Override
	public Type<StopSlammingC2SPayload> type() {
		return TYPE;
	}

	public static void send(double posY) {
		ClientPlayNetworking.send(new StopSlammingC2SPayload(posY));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StopSlammingC2SPayload> {
		@Override
		public void receive(StopSlammingC2SPayload payload, ServerPlayNetworking.Context context) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.get(context.player());
			if (slamComponent.hasSlam() && slamComponent.isSlamming()) {
				slamComponent.stopSlammingServer();
				PlayerLookup.tracking(context.player()).forEach(receiver -> StopSlammingS2CPayload.send(receiver, context.player(), payload.posY()));
			}
		}
	}
}
