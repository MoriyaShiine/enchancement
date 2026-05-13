/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.resources.sound.EMeterSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
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

public record PlayEMeterFloatSoundPayload(int entityId, UUID floatingUuid) implements CustomPacketPayload {
	public static final Type<PlayEMeterFloatSoundPayload> TYPE = new Type<>(Enchancement.id("play_e_meter_float_sound"));
	public static final StreamCodec<FriendlyByteBuf, PlayEMeterFloatSoundPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, PlayEMeterFloatSoundPayload::entityId,
			UUIDUtil.STREAM_CODEC, PlayEMeterFloatSoundPayload::floatingUuid,
			PlayEMeterFloatSoundPayload::new);

	@Override
	public Type<PlayEMeterFloatSoundPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, UUID floatingUuid) {
		ServerPlayNetworking.send(player, new PlayEMeterFloatSoundPayload(entity.getId(), floatingUuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayEMeterFloatSoundPayload> {
		@Override
		public void receive(PlayEMeterFloatSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity != null) {
				ModEntityComponents.E_METER.maybeGet(entity).ifPresent(eMeterComponent -> eMeterComponent.setFloatingUuid(payload.floatingUuid()));
				context.client().getSoundManager().play(new EMeterSoundInstance(entity, payload.floatingUuid()));
			}
		}
	}
}
