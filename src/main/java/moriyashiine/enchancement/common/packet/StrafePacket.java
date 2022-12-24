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
import net.minecraft.util.math.Vec2f;

public class StrafePacket {
	public static final Identifier ID = Enchancement.id("strafe");

	public static void send(Vec2f boost) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeFloat(boost.x);
		buf.writeFloat(boost.y);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		float boostX = buf.readFloat();
		float boostZ = buf.readFloat();
		server.execute(() -> ModEntityComponents.STRAFE.maybeGet(player).ifPresent(strafeComponent -> {
			if (strafeComponent.hasStrafe()) {
				StrafeComponent.handle(player, strafeComponent, boostX, boostZ);
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddStrafeParticlesPacket.send(foundPlayer, player.getId()));
			}
		}));
	}
}
