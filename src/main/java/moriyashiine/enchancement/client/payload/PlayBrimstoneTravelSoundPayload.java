/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.sound.BrimstoneTravelSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record PlayBrimstoneTravelSoundPayload(int entityId) implements CustomPayload {
	public static final Id<PlayBrimstoneTravelSoundPayload> ID = new Id<>(Enchancement.id("play_brimstone_travel_sound"));
	public static final PacketCodec<PacketByteBuf, PlayBrimstoneTravelSoundPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, PlayBrimstoneTravelSoundPayload::entityId,
			PlayBrimstoneTravelSoundPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity) {
		ServerPlayNetworking.send(player, new PlayBrimstoneTravelSoundPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneTravelSoundPayload> {
		@Override
		public void receive(PlayBrimstoneTravelSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof BrimstoneEntity brimstone) {
				context.client().getSoundManager().play(new BrimstoneTravelSoundInstance(brimstone));
			}
		}
	}
}
