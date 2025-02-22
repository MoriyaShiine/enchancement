/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

public record AddEmitterParticlePayload(int entityId, ParticleType<?> particleType) implements CustomPayload {
	public static final Id<AddEmitterParticlePayload> ID = new Id<>(Enchancement.id("add_emitter_particle"));
	public static final PacketCodec<RegistryByteBuf, AddEmitterParticlePayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, AddEmitterParticlePayload::entityId, PacketCodecs.registryCodec(Registries.PARTICLE_TYPE.getCodec()), AddEmitterParticlePayload::particleType, AddEmitterParticlePayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, ParticleType<?> particleType) {
		ServerPlayNetworking.send(player, new AddEmitterParticlePayload(entity.getId(), particleType));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddEmitterParticlePayload> {
		@Override
		public void receive(AddEmitterParticlePayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				context.client().particleManager.addEmitter(entity, (ParticleEffect) payload.particleType());
			}
		}
	}
}
