/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.LightningDashMaceEffect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public record UseLightningDashPayload(int entityId, Vec3d lungeVelocity, int floatTicks) implements CustomPayload {
	public static final Id<UseLightningDashPayload> ID = new Id<>(Enchancement.id("use_lightning_dash"));
	public static final PacketCodec<PacketByteBuf, UseLightningDashPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, UseLightningDashPayload::entityId,
			Vec3d.PACKET_CODEC, UseLightningDashPayload::lungeVelocity,
			PacketCodecs.VAR_INT, UseLightningDashPayload::floatTicks,
			UseLightningDashPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, PlayerEntity user, Vec3d lungeVelocity, int floatTicks) {
		ServerPlayNetworking.send(player, new UseLightningDashPayload(user.getId(), lungeVelocity, floatTicks));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<UseLightningDashPayload> {
		@Override
		public void receive(UseLightningDashPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof PlayerEntity player) {
				LightningDashMaceEffect.useCommon(player, payload.lungeVelocity(), payload.floatTicks());
				LightningDashMaceEffect.useClient(player);
			}
		}
	}
}
