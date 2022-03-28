package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddDashParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class SyncDashPacket {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "sync_dash");

	public static void send(boolean resetFallDistance, Vec3d velocity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(resetFallDistance);
		buf.writeDouble(velocity.getX());
		buf.writeDouble(velocity.getY());
		buf.writeDouble(velocity.getZ());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean resetFallDistance = buf.readBoolean();
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		server.execute(() -> {
			if (resetFallDistance) {
				player.fallDistance = 0;
				player.world.playSoundFromEntity(null, player, ModSoundEvents.ENTITY_GENERIC_DASH, player.getSoundCategory(), 1, 1);
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddDashParticlesPacket.send(foundPlayer, player));
				AddDashParticlesPacket.send(player, player);
			}
			player.setVelocity(new Vec3d(x, y, z));
		});
	}
}
