/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public record SyncHookedMovementDeltaPayload(int entityId, Vec3 delta) implements CustomPacketPayload {
	public static final Type<SyncHookedMovementDeltaPayload> TYPE = new Type<>(Enchancement.id("sync_hooked_movement_delta"));
	public static final StreamCodec<FriendlyByteBuf, SyncHookedMovementDeltaPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SyncHookedMovementDeltaPayload::entityId,
			Vec3.STREAM_CODEC, SyncHookedMovementDeltaPayload::delta,
			SyncHookedMovementDeltaPayload::new);

	@Override
	public Type<SyncHookedMovementDeltaPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, Vec3 delta) {
		ServerPlayNetworking.send(player, new SyncHookedMovementDeltaPayload(entity.getId(), delta));
	}

	public static Set<Entity> getEntities(Entity root) {
		Set<Entity> entities = new HashSet<>(root.getRootVehicle().getPassengers());
		entities.add(root.getRootVehicle());
		return entities;
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncHookedMovementDeltaPayload> {
		@Override
		public void receive(SyncHookedMovementDeltaPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity != null) {
				getEntities(entity).forEach(foundEntity -> foundEntity.setDeltaMovement(payload.delta()));
			}
		}
	}
}
