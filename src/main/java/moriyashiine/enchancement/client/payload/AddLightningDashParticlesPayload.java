/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;

public record AddLightningDashParticlesPayload(int entityId) implements CustomPacketPayload {
	public static final Type<AddLightningDashParticlesPayload> TYPE = new Type<>(Enchancement.id("add_lightning_dash_particles"));
	public static final StreamCodec<FriendlyByteBuf, AddLightningDashParticlesPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, AddLightningDashParticlesPayload::entityId,
			AddLightningDashParticlesPayload::new);

	@Override
	public Type<AddLightningDashParticlesPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Entity entity) {
		ServerPlayNetworking.send(player, new AddLightningDashParticlesPayload(entity.getId()));
	}

	public static void addParticles(Entity entity) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		double y = Math.round(entity.level().clip(new ClipContext(entity.position(), entity.position().add(entity.getLookAngle().scale(4)), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity)).getLocation().y() - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 7; j++) {
				double x = entity.getX() + Mth.sin(i) * j / 2, z = entity.getZ() + Mth.cos(i) * j / 2;
				BlockState state = entity.level().getBlockState(mutable.set(x, y, z));
				if (!state.canBeReplaced() && entity.level().getBlockState(mutable.move(Direction.UP)).canBeReplaced()) {
					BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 8; k++) {
						entity.level().addParticle(particle, x, mutable.getY() + 0.5, z, 0, 0, 0);
					}
				}
			}
		}
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddLightningDashParticlesPayload> {
		@Override
		public void receive(AddLightningDashParticlesPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			if (entity != null) {
				addParticles(entity);
			}
		}
	}
}
