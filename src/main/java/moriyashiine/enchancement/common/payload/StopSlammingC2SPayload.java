/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StopSlammingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StopSlammingC2SPayload(double posY) implements CustomPayload {
	public static final Id<StopSlammingC2SPayload> ID = new Id<>(Enchancement.id("stop_slamming_c2s"));
	public static final PacketCodec<PacketByteBuf, StopSlammingC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.DOUBLE, StopSlammingC2SPayload::posY, StopSlammingC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
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
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> StopSlammingS2CPayload.send(foundPlayer, context.player(), payload.posY()));
			}
		}
	}
}
