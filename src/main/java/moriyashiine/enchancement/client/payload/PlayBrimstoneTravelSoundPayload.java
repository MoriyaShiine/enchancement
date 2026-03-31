/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.resources.sound.BrimstoneTravelSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record PlayBrimstoneTravelSoundPayload(int entityId) implements CustomPacketPayload {
	public static final Type<PlayBrimstoneTravelSoundPayload> TYPE = new Type<>(Enchancement.id("play_brimstone_travel_sound"));
	public static final StreamCodec<FriendlyByteBuf, PlayBrimstoneTravelSoundPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, PlayBrimstoneTravelSoundPayload::entityId,
			PlayBrimstoneTravelSoundPayload::new);

	@Override
	public Type<PlayBrimstoneTravelSoundPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity) {
		ServerPlayNetworking.send(player, new PlayBrimstoneTravelSoundPayload(entity.getId()));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneTravelSoundPayload> {
		@Override
		public void receive(PlayBrimstoneTravelSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Brimstone brimstone) {
				context.client().getSoundManager().play(new BrimstoneTravelSoundInstance(brimstone));
			}
		}
	}
}
