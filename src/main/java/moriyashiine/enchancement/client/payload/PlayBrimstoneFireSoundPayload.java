/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.resources.sound.BrimstoneFireSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public record PlayBrimstoneFireSoundPayload(int entityId, UUID uuid) implements CustomPacketPayload {
	public static final Type<PlayBrimstoneFireSoundPayload> TYPE = new Type<>(Enchancement.id("play_brimstone_fire_sound"));
	public static final StreamCodec<FriendlyByteBuf, PlayBrimstoneFireSoundPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, PlayBrimstoneFireSoundPayload::entityId,
			UUIDUtil.STREAM_CODEC, PlayBrimstoneFireSoundPayload::uuid,
			PlayBrimstoneFireSoundPayload::new);

	@Override
	public Type<PlayBrimstoneFireSoundPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, UUID uuid) {
		ServerPlayNetworking.send(player, new PlayBrimstoneFireSoundPayload(entity.getId(), uuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneFireSoundPayload> {
		@Override
		public void receive(PlayBrimstoneFireSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity != null) {
				context.client().getSoundManager().play(new BrimstoneFireSoundInstance(entity, payload.uuid()));
			}
		}
	}
}
