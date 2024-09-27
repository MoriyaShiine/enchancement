/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.BoostInFluidComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BoostInFluidPayload(boolean shouldBoost) implements CustomPayload {
	public static final CustomPayload.Id<BoostInFluidPayload> ID = new Id<>(Enchancement.id("boost_in_fluid"));
	public static final PacketCodec<PacketByteBuf, BoostInFluidPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, BoostInFluidPayload::shouldBoost, BoostInFluidPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(boolean shouldBoost) {
		ClientPlayNetworking.send(new BoostInFluidPayload(shouldBoost));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BoostInFluidPayload> {
		@Override
		public void receive(BoostInFluidPayload payload, ServerPlayNetworking.Context context) {
			BoostInFluidComponent boostInFluidComponent = ModEntityComponents.BOOST_IN_FLUID.get(context.player());
			if (boostInFluidComponent.hasBoost() && boostInFluidComponent.canUse(true)) {
				boostInFluidComponent.setShouldBoost(payload.shouldBoost());
			}
		}
	}
}
