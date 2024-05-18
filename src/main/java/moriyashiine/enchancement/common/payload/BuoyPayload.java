/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.BuoyComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BuoyPayload(boolean shouldBoost) implements CustomPayload {
	public static final CustomPayload.Id<BuoyPayload> ID = CustomPayload.id(Enchancement.id("buoy").toString());
	public static final PacketCodec<PacketByteBuf, BuoyPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, BuoyPayload::shouldBoost, BuoyPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(boolean shouldBoost) {
		ClientPlayNetworking.send(new BuoyPayload(shouldBoost));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BuoyPayload> {
		@Override
		public void receive(BuoyPayload payload, ServerPlayNetworking.Context context) {
			BuoyComponent buoyComponent = ModEntityComponents.BUOY.get(context.player());
			if (buoyComponent.hasBuoy() && buoyComponent.canUse()) {
				buoyComponent.setShouldBoost(payload.shouldBoost());
			}
		}
	}
}
