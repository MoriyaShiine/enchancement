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

public class SyncJumpingPacket {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "sync_jumping");

	public static void send(boolean jumping) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(jumping);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean jumping = buf.readBoolean();
		server.execute(() -> ModEntityComponents.JUMPING.maybeGet(player).ifPresent(jumpingComponent -> {
			jumpingComponent.setJumping(jumping);
			jumpingComponent.sync();
		}));
	}
}
