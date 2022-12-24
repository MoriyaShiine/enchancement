/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AddMoltenParticlesPacket {
	public static final Identifier ID = Enchancement.id("add_molten_particles");

	public static void send(ServerPlayerEntity player, BlockPos pos) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		long longPos = buf.readLong();
		client.execute(() -> {
			ClientWorld world = client.world;
			if (world != null) {
				BlockPos pos = BlockPos.fromLong(longPos);
				for (int i = 0; i < 8; i++) {
					world.addParticle(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), pos.getY() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.5, 0.5F), 0, 0, 0);
				}
			}
		});
	}
}
