/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

public record SyncHookedVelocityPayload(int entityId, Vec3d velocity) implements CustomPayload {
	public static final Id<SyncHookedVelocityPayload> ID = new Id<>(Enchancement.id("sync_hooked_velocity"));
	public static final PacketCodec<PacketByteBuf, SyncHookedVelocityPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, SyncHookedVelocityPayload::entityId, Vec3d.PACKET_CODEC, SyncHookedVelocityPayload::velocity, SyncHookedVelocityPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, Vec3d velocity) {
		ServerPlayNetworking.send(player, new SyncHookedVelocityPayload(entity.getId(), velocity));
	}

	public static Set<Entity> getEntities(Entity root) {
		Set<Entity> entities = new HashSet<>(root.getRootVehicle().getPassengerList());
		entities.add(root.getRootVehicle());
		return entities;
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncHookedVelocityPayload> {
		@Override
		public void receive(SyncHookedVelocityPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getEntityWorld().getEntityById(payload.entityId());
			if (entity != null) {
				getEntities(entity).forEach(foundEntity -> foundEntity.setVelocity(payload.velocity()));
			}
		}
	}
}
