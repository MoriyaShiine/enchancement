/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.sound.BrimstoneFireSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record PlayBrimstoneFireSoundPayload(int entityId, UUID uuid) implements CustomPayload {
	public static final CustomPayload.Id<PlayBrimstoneFireSoundPayload> ID = new Id<>(Enchancement.id("play_brimstone_fire_sound"));
	public static final PacketCodec<PacketByteBuf, PlayBrimstoneFireSoundPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PlayBrimstoneFireSoundPayload::entityId, Uuids.PACKET_CODEC, PlayBrimstoneFireSoundPayload::uuid, PlayBrimstoneFireSoundPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int entityId, UUID uuid) {
		ServerPlayNetworking.send(player, new PlayBrimstoneFireSoundPayload(entityId, uuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneFireSoundPayload> {
		@Override
		public void receive(PlayBrimstoneFireSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				context.client().getSoundManager().play(new BrimstoneFireSoundInstance(entity, payload.uuid(), entity.getSoundCategory()));
			}
		}
	}
}
