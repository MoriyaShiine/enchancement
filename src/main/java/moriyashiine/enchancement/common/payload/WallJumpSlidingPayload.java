/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record WallJumpSlidingPayload(int entityId, long pos) implements CustomPacketPayload {
	public static final Type<WallJumpSlidingPayload> TYPE = new Type<>(Enchancement.id("wall_jump_sliding"));
	public static final StreamCodec<FriendlyByteBuf, WallJumpSlidingPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, WallJumpSlidingPayload::entityId,
			ByteBufCodecs.VAR_LONG, WallJumpSlidingPayload::pos,
			WallJumpSlidingPayload::new
	);

	private static final long PLACEHOLDER_NULL = Long.MAX_VALUE;

	@Override
	public Type<WallJumpSlidingPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, BlockPos pos) {
		ClientPlayNetworking.send(new WallJumpSlidingPayload(entity.getId(), pos == null ? PLACEHOLDER_NULL : pos.asLong()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<WallJumpSlidingPayload> {
		@Override
		public void receive(WallJumpSlidingPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				BlockPos pos = payload.pos() == PLACEHOLDER_NULL ? null : BlockPos.of(payload.pos());
				ModEntityComponents.WALL_JUMP.get(entity).setSlidingPos(pos);
			}
		}
	}
}
