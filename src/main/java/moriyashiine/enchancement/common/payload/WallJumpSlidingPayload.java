/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record WallJumpSlidingPayload(int entityId, long pos) implements CustomPayload {
	public static final Id<WallJumpSlidingPayload> ID = new Id<>(Enchancement.id("wall_jump_sliding"));
	public static final PacketCodec<PacketByteBuf, WallJumpSlidingPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, WallJumpSlidingPayload::entityId,
			PacketCodecs.VAR_LONG, WallJumpSlidingPayload::pos,
			WallJumpSlidingPayload::new
	);

	private static final long PLACEHOLDER_NULL = Long.MAX_VALUE;

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Entity entity, BlockPos pos) {
		ClientPlayNetworking.send(new WallJumpSlidingPayload(entity.getId(), pos == null ? PLACEHOLDER_NULL : pos.asLong()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<WallJumpSlidingPayload> {
		@Override
		public void receive(WallJumpSlidingPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity instanceof LivingEntity) {
				BlockPos pos = payload.pos() == PLACEHOLDER_NULL ? null : BlockPos.fromLong(payload.pos());
				ModEntityComponents.WALL_JUMP.get(entity).setSlidingPos(pos);
			}
		}
	}
}
