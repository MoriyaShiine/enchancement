/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public record AddMoltenParticlesPayload(BlockPos pos) implements CustomPacketPayload {
	public static final Type<AddMoltenParticlesPayload> TYPE = new Type<>(Enchancement.id("add_molten_particles"));
	public static final StreamCodec<FriendlyByteBuf, AddMoltenParticlesPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, AddMoltenParticlesPayload::pos,
			AddMoltenParticlesPayload::new);

	@Override
	public Type<AddMoltenParticlesPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, BlockPos pos) {
		ServerPlayNetworking.send(player, new AddMoltenParticlesPayload(pos));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddMoltenParticlesPayload> {
		@Override
		public void receive(AddMoltenParticlesPayload payload, ClientPlayNetworking.Context context) {
			ClientLevel level = context.client().level;
			if (level != null) {
				for (int i = 0; i < 8; i++) {
					level.addParticle(ModParticleTypes.SHORT_SMALL_FLAME, payload.pos().getX() + 0.5 + Mth.nextDouble(level.getRandom(), -0.5, 0.5F), payload.pos().getY() + 0.5 + Mth.nextDouble(level.getRandom(), -0.5, 0.5F), payload.pos().getZ() + 0.5 + Mth.nextDouble(level.getRandom(), -0.5, 0.5F), 0, 0, 0);
				}
			}
		}
	}
}
