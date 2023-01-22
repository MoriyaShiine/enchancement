/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class SlideVelocityPacket {
	public static final Identifier ID = Enchancement.id("slide_velocity");

	public static void send(Vec3d velocity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeFloat((float) velocity.getX());
		buf.writeFloat((float) velocity.getZ());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		float velocityX = buf.readFloat();
		float velocityZ = buf.readFloat();
		server.execute(() -> ModEntityComponents.SLIDE.maybeGet(player).ifPresent(slideComponent -> {
			if (slideComponent.hasSlide()) {
				slideComponent.setVelocity(new Vec3d(velocityX, 0, velocityZ));
			}
		}));
	}
}
