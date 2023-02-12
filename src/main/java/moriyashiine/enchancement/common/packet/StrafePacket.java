/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddStrafeParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class StrafePacket {
	public static final Identifier ID = Enchancement.id("strafe");

	public static void send(Vec3d velocity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeFloat((float) velocity.getX());
		buf.writeFloat((float) velocity.getZ());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		float velocityX = buf.readFloat();
		float velocityZ = buf.readFloat();
		server.execute(() -> ModEntityComponents.STRAFE.maybeGet(player).ifPresent(strafeComponent -> {
			if (strafeComponent.hasStrafe()) {
				StrafeComponent.handle(player, strafeComponent, velocityX, velocityZ);
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddStrafeParticlesPacket.send(foundPlayer, player.getId()));
			}
		}));
	}
}
