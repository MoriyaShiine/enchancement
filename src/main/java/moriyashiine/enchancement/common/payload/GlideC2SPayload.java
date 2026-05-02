/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.GlideS2CPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public record GlideC2SPayload(int entityId, boolean gliding) implements CustomPacketPayload {
	public static final Type<GlideC2SPayload> TYPE = new Type<>(Enchancement.id("glide_c2s"));
	public static final StreamCodec<FriendlyByteBuf, GlideC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, GlideC2SPayload::entityId,
			ByteBufCodecs.BOOL, GlideC2SPayload::gliding,
			GlideC2SPayload::new);

	@Override
	public Type<GlideC2SPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, boolean gliding) {
		ClientPlayNetworking.send(new GlideC2SPayload(entity.getId(), gliding));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GlideC2SPayload> {
		@Override
		public void receive(GlideC2SPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			ModEntityComponents.GLIDE.maybeGet(entity).ifPresent(glideComponent -> {
				if (glideComponent.canGlide()) {
					glideComponent.setGliding(payload.gliding());
					PlayerLookup.tracking(entity).forEach(receiver -> GlideS2CPayload.send(receiver, entity, payload.gliding()));
				}
			});
		}
	}
}
