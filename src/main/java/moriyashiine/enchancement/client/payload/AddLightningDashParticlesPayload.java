/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;

public record AddLightningDashParticlesPayload(int entityId) implements CustomPayload {
	public static final Id<AddLightningDashParticlesPayload> ID = new Id<>(Enchancement.id("add_lightning_dash_particles"));
	public static final PacketCodec<PacketByteBuf, AddLightningDashParticlesPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, AddLightningDashParticlesPayload::entityId,
			AddLightningDashParticlesPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Entity entity) {
		ServerPlayNetworking.send(player, new AddLightningDashParticlesPayload(entity.getId()));
	}

	public static void addParticles(Entity entity) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		double y = Math.round(entity.getWorld().raycast(new RaycastContext(entity.getPos(), entity.getPos().add(entity.getRotationVector().multiply(4)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, entity)).getPos().getY() - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 7; j++) {
				double x = entity.getX() + MathHelper.sin(i) * j / 2, z = entity.getZ() + MathHelper.cos(i) * j / 2;
				BlockState state = entity.getWorld().getBlockState(mutable.set(x, y, z));
				if (!state.isReplaceable() && entity.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
					BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 8; k++) {
						entity.getWorld().addParticleClient(particle, x, mutable.getY() + 0.5, z, 0, 0, 0);
					}
				}
			}
		}
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddLightningDashParticlesPayload> {
		@Override
		public void receive(AddLightningDashParticlesPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				addParticles(entity);
			}
		}
	}
}
