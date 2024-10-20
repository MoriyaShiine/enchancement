/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.HashSet;
import java.util.Set;

public record SyncBounceInvertedStatusPayload(boolean inverted) implements CustomPayload {
	public static final Id<SyncBounceInvertedStatusPayload> ID = new Id<>(Enchancement.id("sync_bounce_inverted_status"));
	public static final PacketCodec<PacketByteBuf, SyncBounceInvertedStatusPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.BOOL, SyncBounceInvertedStatusPayload::inverted,
			SyncBounceInvertedStatusPayload::new
	);

	public static final Set<Entity> ENTITIES_WITH_INVERTED_STATUS = new HashSet<>();

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(boolean inverted) {
		ClientPlayNetworking.send(new SyncBounceInvertedStatusPayload(inverted));
	}

	public static void toggle(Entity entity, boolean inverted) {
		if (inverted) {
			ENTITIES_WITH_INVERTED_STATUS.add(entity);
		} else {
			ENTITIES_WITH_INVERTED_STATUS.remove(entity);
		}
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncBounceInvertedStatusPayload> {
		@Override
		public void receive(SyncBounceInvertedStatusPayload payload, ServerPlayNetworking.Context context) {
			toggle(context.player(), payload.inverted());
		}
	}
}
