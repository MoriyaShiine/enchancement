/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record SlideResetVelocityPayload() implements CustomPayload {
	public static final CustomPayload.Id<SlideResetVelocityPayload> ID = CustomPayload.id(Enchancement.id("slide_reset_velocity").toString());
	public static final PacketCodec<PacketByteBuf, SlideResetVelocityPayload> CODEC = PacketCodec.unit(new SlideResetVelocityPayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new SlideResetVelocityPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SlideResetVelocityPayload> {
		@Override
		public void receive(SlideResetVelocityPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide()) {
				slideComponent.setVelocity(Vec3d.ZERO);
			}
		}
	}
}
