/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.SyncVelocitiesEvent;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record SyncVelocityPayload(Vec3d velocity) implements CustomPayload {
	public static final Id<SyncVelocityPayload> ID = new Id<>(Enchancement.id("sync_velocity"));
	public static final PacketCodec<PacketByteBuf, SyncVelocityPayload> CODEC = PacketCodec.tuple(
			EnchancementUtil.VEC3D_PACKET_CODEC, SyncVelocityPayload::velocity,
			SyncVelocityPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new SyncVelocityPayload(velocity));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncVelocityPayload> {
		@Override
		public void receive(SyncVelocityPayload payload, ServerPlayNetworking.Context context) {
			SyncVelocitiesEvent.VELOCITIES.put(context.player().getUuid(), payload.velocity());
		}
	}
}
