/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.GlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record GlideS2CPayload(int entityId, boolean gliding) implements CustomPayload {
	public static final Id<GlideS2CPayload> ID = new Id<>(Enchancement.id("glide_s2c"));
	public static final PacketCodec<PacketByteBuf, GlideS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, GlideS2CPayload::entityId, PacketCodecs.BOOLEAN, GlideS2CPayload::gliding, GlideS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, boolean gliding) {
		ServerPlayNetworking.send(player, new GlideS2CPayload(entity.getId(), gliding));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<GlideS2CPayload> {
		@Override
		public void receive(GlideS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof LivingEntity living) {
				GlideComponent glideComponent = ModEntityComponents.GLIDE.get(living);
				if (glideComponent.canGlide()) {
					glideComponent.setGliding(payload.gliding());
				}
			}
		}
	}
}
