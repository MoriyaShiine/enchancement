/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SyncEnchantingMaterialMapPacket {
	public static final Identifier ID = Enchancement.id("sync_enchanting_material_map");

	public static void send(ServerPlayerEntity player) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.size());
		EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.forEach((item, ingredient) -> {
			buf.writeIdentifier(Registries.ITEM.getId(item));
			ingredient.write(buf);
		});
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int size = buf.readInt();
			Map<Item, Ingredient> readMap = new HashMap<>();
			for (int i = 0; i < size; i++) {
				readMap.put(Registries.ITEM.get(buf.readIdentifier()), Ingredient.fromPacket(buf));
			}
			client.execute(() -> {
				EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.clear();
				EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.putAll(readMap);
			});
		}
	}
}
