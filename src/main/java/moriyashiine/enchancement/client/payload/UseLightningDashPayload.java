/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.effect.LightningDashMaceEffect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record UseLightningDashPayload(int entityId, Vec3 lungeDelta, int floatTicks) implements CustomPacketPayload {
	public static final Type<UseLightningDashPayload> TYPE = new Type<>(Enchancement.id("use_lightning_dash"));
	public static final StreamCodec<FriendlyByteBuf, UseLightningDashPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UseLightningDashPayload::entityId,
			Vec3.STREAM_CODEC, UseLightningDashPayload::lungeDelta,
			ByteBufCodecs.VAR_INT, UseLightningDashPayload::floatTicks,
			UseLightningDashPayload::new);

	@Override
	public Type<UseLightningDashPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Player user, Vec3 lungeDelta, int floatTicks) {
		ServerPlayNetworking.send(player, new UseLightningDashPayload(user.getId(), lungeDelta, floatTicks));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<UseLightningDashPayload> {
		@Override
		public void receive(UseLightningDashPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof Player player) {
				LightningDashMaceEffect.useCommon(player, payload.lungeDelta(), payload.floatTicks());
				LightningDashMaceEffect.useClient(player);
			}
		}
	}
}
