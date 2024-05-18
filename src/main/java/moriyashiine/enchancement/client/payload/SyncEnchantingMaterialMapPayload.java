/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.payload;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public record SyncEnchantingMaterialMapPayload(Map<RegistryEntry<Item>, Ingredient> map) implements CustomPayload {
	public static final CustomPayload.Id<SyncEnchantingMaterialMapPayload> ID = CustomPayload.id(Enchancement.id("sync_enchanting_material_map").toString());
	public static final PacketCodec<RegistryByteBuf, SyncEnchantingMaterialMapPayload> CODEC = PacketCodec.tuple(PacketCodecs.map(Object2ObjectOpenHashMap::new, PacketCodecs.registryEntry(RegistryKeys.ITEM), Ingredient.PACKET_CODEC), SyncEnchantingMaterialMapPayload::map, SyncEnchantingMaterialMapPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, new SyncEnchantingMaterialMapPayload(EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingMaterialMapPayload> {
		@Override
		public void receive(SyncEnchantingMaterialMapPayload payload, ClientPlayNetworking.Context context) {
			EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.clear();
			EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.putAll(payload.map());
		}
	}
}
