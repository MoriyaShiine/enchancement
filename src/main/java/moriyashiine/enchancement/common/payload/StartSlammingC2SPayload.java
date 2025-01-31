/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.StartSlammingS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record StartSlammingC2SPayload() implements CustomPayload {
	public static final CustomPayload.Id<StartSlammingC2SPayload> ID = new Id<>(Enchancement.id("start_slamming_cs2"));
	public static final PacketCodec<PacketByteBuf, StartSlammingC2SPayload> CODEC = PacketCodec.unit(new StartSlammingC2SPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new StartSlammingC2SPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StartSlammingC2SPayload> {
		@Override
		public void receive(StartSlammingC2SPayload payload, ServerPlayNetworking.Context context) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.get(context.player());
			if (slamComponent.hasSlam() && slamComponent.canSlam()) {
				slamComponent.setSlamming(true);
				slamComponent.setSlamCooldown(SlamComponent.DEFAULT_SLAM_COOLDOWN);
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> StartSlammingS2CPayload.send(foundPlayer, context.player()));
			}
		}
	}
}
