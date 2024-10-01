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
import net.minecraft.sound.SoundCategory;

public record PlayBrimstoneTravelSoundPayload(int entityId, String soundCategory) implements CustomPayload {
	public static final Id<PlayBrimstoneTravelSoundPayload> ID = new Id<>(Enchancement.id("play_brimstone_travel_sound"));
	public static final PacketCodec<PacketByteBuf, PlayBrimstoneTravelSoundPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, PlayBrimstoneTravelSoundPayload::entityId,
			PacketCodecs.STRING, PlayBrimstoneTravelSoundPayload::soundCategory,
			PlayBrimstoneTravelSoundPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity, SoundCategory soundCategory) {
		ServerPlayNetworking.send(player, new PlayBrimstoneTravelSoundPayload(entity.getId(), soundCategory.name()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneTravelSoundPayload> {
		@Override
		public void receive(PlayBrimstoneTravelSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof BrimstoneEntity brimstone) {
				context.client().getSoundManager().play(new BrimstoneTravelSoundInstance(brimstone, SoundCategory.valueOf(payload.soundCategory())));
			}
		}
	}
}
