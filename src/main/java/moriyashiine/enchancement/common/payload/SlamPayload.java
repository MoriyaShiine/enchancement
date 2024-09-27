/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SlamPayload() implements CustomPayload {
	public static final CustomPayload.Id<SlamPayload> ID = new Id<>(Enchancement.id("slam"));
	public static final PacketCodec<PacketByteBuf, SlamPayload> CODEC = PacketCodec.unit(new SlamPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new SlamPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SlamPayload> {
		@Override
		public void receive(SlamPayload payload, ServerPlayNetworking.Context context) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.get(context.player());
			if (slamComponent.hasSlam() && slamComponent.canSlam()) {
				slamComponent.setSlamming(true);
				slamComponent.setSlamCooldown(SlamComponent.DEFAULT_SLAM_COOLDOWN);
			}
		}
	}
}
