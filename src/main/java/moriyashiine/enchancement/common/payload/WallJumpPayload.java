/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record WallJumpPayload(Vec3d velocity) implements CustomPayload {
	public static final Id<WallJumpPayload> ID = new Id<>(Enchancement.id("wall_jump"));
	public static final PacketCodec<PacketByteBuf, WallJumpPayload> CODEC = PacketCodec.tuple(
			EnchancementUtil.VEC3D_PACKET_CODEC, WallJumpPayload::velocity,
			WallJumpPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new WallJumpPayload(velocity));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<WallJumpPayload> {
		@Override
		public void receive(WallJumpPayload payload, ServerPlayNetworking.Context context) {
			ModEntityComponents.WALL_JUMP.get(context.player()).use(payload.velocity());
		}
	}
}
