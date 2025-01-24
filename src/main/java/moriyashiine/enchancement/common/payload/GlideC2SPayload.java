/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.GlideS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.GlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record GlideC2SPayload(boolean gliding) implements CustomPayload {
	public static final Id<GlideC2SPayload> ID = new Id<>(Enchancement.id("glide_c2s"));
	public static final PacketCodec<PacketByteBuf, GlideC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, GlideC2SPayload::gliding, GlideC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(boolean gliding) {
		ClientPlayNetworking.send(new GlideC2SPayload(gliding));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GlideC2SPayload> {
		@Override
		public void receive(GlideC2SPayload payload, ServerPlayNetworking.Context context) {
			GlideComponent glideComponent = ModEntityComponents.GLIDE.get(context.player());
			if (glideComponent.canGlide()) {
				glideComponent.setGliding(payload.gliding());
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> GlideS2CPayload.send(foundPlayer, context.player(), payload.gliding()));
			}
		}
	}
}
