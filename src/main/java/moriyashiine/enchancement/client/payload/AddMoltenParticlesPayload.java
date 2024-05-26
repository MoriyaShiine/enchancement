/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public record AddMoltenParticlesPayload(BlockPos pos) implements CustomPayload {
	public static final CustomPayload.Id<AddMoltenParticlesPayload> ID = CustomPayload.id(Enchancement.id("add_molten_particles").toString());
	public static final PacketCodec<PacketByteBuf, AddMoltenParticlesPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, AddMoltenParticlesPayload::pos, AddMoltenParticlesPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, BlockPos pos) {
		ServerPlayNetworking.send(player, new AddMoltenParticlesPayload(pos));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddMoltenParticlesPayload> {
		@Override
		public void receive(AddMoltenParticlesPayload payload, ClientPlayNetworking.Context context) {
			ClientWorld world = context.client().world;
			if (world != null) {
				for (int i = 0; i < 8; i++) {
					world.addParticle(ParticleTypes.SMALL_FLAME, payload.pos().getX() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), payload.pos().getY() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), payload.pos().getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), 0, 0, 0);
				}
			}
		}
	}
}
