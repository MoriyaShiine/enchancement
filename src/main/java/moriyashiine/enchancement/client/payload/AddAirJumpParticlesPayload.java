/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;

public record AddAirJumpParticlesPayload(int entityId) implements CustomPayload {
	public static final CustomPayload.Id<AddAirJumpParticlesPayload> ID = new Id<>(Enchancement.id("add_air_jump_particles"));
	public static final PacketCodec<PacketByteBuf, AddAirJumpParticlesPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, AddAirJumpParticlesPayload::entityId, AddAirJumpParticlesPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int id) {
		ServerPlayNetworking.send(player, new AddAirJumpParticlesPayload(id));
	}

	public static void addParticles(Entity entity) {
		if (EnchancementClientUtil.shouldAddParticles(entity)) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddAirJumpParticlesPayload> {
		@Override
		public void receive(AddAirJumpParticlesPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				addParticles(entity);
			}
		}
	}
}
