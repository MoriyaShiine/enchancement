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
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record SlideSetVelocityPayload(float velocityX, float velocityZ) implements CustomPayload {
	public static final CustomPayload.Id<SlideSetVelocityPayload> ID = CustomPayload.id(Enchancement.id("slide_set_velocity").toString());
	public static final PacketCodec<PacketByteBuf, SlideSetVelocityPayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, SlideSetVelocityPayload::velocityX, PacketCodecs.FLOAT, SlideSetVelocityPayload::velocityZ, SlideSetVelocityPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new SlideSetVelocityPayload((float) velocity.getX(), (float) velocity.getZ()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SlideSetVelocityPayload> {
		@Override
		public void receive(SlideSetVelocityPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide() && slideComponent.canSlide()) {
				slideComponent.setVelocity(new Vec3d(payload.velocityX(), 0, payload.velocityZ()));
			}
		}
	}
}
