/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.sound.SparkSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record PlaySparkSoundPayload(int entityId) implements CustomPayload {
	public static final Id<PlaySparkSoundPayload> ID = new Id<>(Enchancement.id("play_spark_sound"));
	public static final PacketCodec<PacketByteBuf, PlaySparkSoundPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PlaySparkSoundPayload::entityId, PlaySparkSoundPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int entityId) {
		ServerPlayNetworking.send(player, new PlaySparkSoundPayload(entityId));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlaySparkSoundPayload> {
		@Override
		public void receive(PlaySparkSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				context.client().getSoundManager().play(new SparkSoundInstance(entity));
			}
		}
	}
}
