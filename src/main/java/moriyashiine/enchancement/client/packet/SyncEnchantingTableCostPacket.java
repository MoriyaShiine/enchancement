/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.screen.EnchantingTableScreen;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncEnchantingTableCostPacket implements ClientPlayNetworking.PlayChannelHandler {
	public static final Identifier ID = Enchancement.id("sync_enchanting_table_cost");

	public static void send(ServerPlayerEntity player, int cost) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(cost);
		ServerPlayNetworking.send(player, ID, buf);
	}

	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int cost = buf.readInt();
		client.execute(() -> {
			if (client.currentScreen instanceof EnchantingTableScreen enchantingTableScreen) {
				enchantingTableScreen.getScreenHandler().setCost(cost);
			}
		});
	}
}
