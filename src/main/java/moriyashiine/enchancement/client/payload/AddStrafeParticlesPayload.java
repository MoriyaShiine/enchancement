/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record AddStrafeParticlesPayload(int entityId) implements CustomPayload {
	public static final CustomPayload.Id<AddStrafeParticlesPayload> ID = CustomPayload.id(Enchancement.id("add_strafe_particles").toString());
	public static final PacketCodec<PacketByteBuf, AddStrafeParticlesPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, AddStrafeParticlesPayload::entityId, AddStrafeParticlesPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int id) {
		ServerPlayNetworking.send(player, new AddStrafeParticlesPayload(id));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddStrafeParticlesPayload> {
		@Override
		public void receive(AddStrafeParticlesPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				StrafeComponent.addStrafeParticles(entity);
			}
		}
	}
}
