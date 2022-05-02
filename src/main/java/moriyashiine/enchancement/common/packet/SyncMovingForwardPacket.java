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

public class SyncMovingForwardPacket {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "sync_moving_forward");

	public static void send(boolean movingForward) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(movingForward);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean movingForward = buf.readBoolean();
		server.execute(() -> ModEntityComponents.MOVING_FORWARD.maybeGet(player).ifPresent(movingForwardComponent -> {
			movingForwardComponent.setMovingForward(movingForward);
			movingForwardComponent.sync();
		}));
	}
}
