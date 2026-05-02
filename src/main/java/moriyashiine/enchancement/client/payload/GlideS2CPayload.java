/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record GlideS2CPayload(int entityId, boolean gliding) implements CustomPacketPayload {
	public static final Type<GlideS2CPayload> TYPE = new Type<>(Enchancement.id("glide_s2c"));
	public static final StreamCodec<FriendlyByteBuf, GlideS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, GlideS2CPayload::entityId,
			ByteBufCodecs.BOOL, GlideS2CPayload::gliding,
			GlideS2CPayload::new);

	@Override
	public Type<GlideS2CPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity, boolean gliding) {
		ServerPlayNetworking.send(player, new GlideS2CPayload(entity.getId(), gliding));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<GlideS2CPayload> {
		@Override
		public void receive(GlideS2CPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.GLIDE.maybeGet(entity).ifPresent(glideComponent -> {
				if (glideComponent.canGlide()) {
					glideComponent.setGliding(payload.gliding());
				}
			});
		}
	}
}
