/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public record WallJumpPayload(int entityId, Vec3 delta) implements CustomPacketPayload {
	public static final Type<WallJumpPayload> TYPE = new Type<>(Enchancement.id("wall_jump"));
	public static final StreamCodec<FriendlyByteBuf, WallJumpPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, WallJumpPayload::entityId,
			Vec3.STREAM_CODEC, WallJumpPayload::delta,
			WallJumpPayload::new
	);

	@Override
	public Type<WallJumpPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity, Vec3 delta) {
		ClientPlayNetworking.send(new WallJumpPayload(entity.getId(), delta));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<WallJumpPayload> {
		@Override
		public void receive(WallJumpPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity instanceof LivingEntity) {
				ModEntityComponents.WALL_JUMP.get(entity).use(payload.delta());
			}
		}
	}
}
