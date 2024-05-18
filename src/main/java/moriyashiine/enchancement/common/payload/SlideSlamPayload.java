/*
 * All Rights Reserved (c) MoriyaShiine
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

public record SlideSlamPayload() implements CustomPayload {
	public static final CustomPayload.Id<SlideSlamPayload> ID = CustomPayload.id(Enchancement.id("slide_slam").toString());
	public static final PacketCodec<PacketByteBuf, SlideSlamPayload> CODEC = PacketCodec.unit(new SlideSlamPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new SlideSlamPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SlideSlamPayload> {
		@Override
		public void receive(SlideSlamPayload payload, ServerPlayNetworking.Context context) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(context.player());
			if (slideComponent.hasSlide() && slideComponent.canSlam()) {
				slideComponent.setShouldSlam(true);
				slideComponent.setSlamCooldown(SlideComponent.DEFAULT_SLAM_COOLDOWN);
			}
		}
	}
}
