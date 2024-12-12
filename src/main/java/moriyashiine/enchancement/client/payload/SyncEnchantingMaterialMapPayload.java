/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public record SyncEnchantingMaterialMapPayload(
		Map<RegistryEntry<Item>, EnchantingTableScreenHandler.EnchantingMaterial> map) implements CustomPayload {
	public static final CustomPayload.Id<SyncEnchantingMaterialMapPayload> ID = new Id<>(Enchancement.id("sync_enchanting_material_map"));
	public static final PacketCodec<RegistryByteBuf, SyncEnchantingMaterialMapPayload> CODEC = PacketCodec.tuple(PacketCodecs.map(Object2ObjectOpenHashMap::new, PacketCodecs.registryEntry(RegistryKeys.ITEM), EnchantingTableScreenHandler.EnchantingMaterial.PACKET_CODEC), SyncEnchantingMaterialMapPayload::map, SyncEnchantingMaterialMapPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player) {
		Map<RegistryEntry<Item>, EnchantingTableScreenHandler.EnchantingMaterial> map = new HashMap<>();
		EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.forEach((key, value) -> map.put(Registries.ITEM.getEntry(key), value));
		ServerPlayNetworking.send(player, new SyncEnchantingMaterialMapPayload(map));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingMaterialMapPayload> {
		@Override
		public void receive(SyncEnchantingMaterialMapPayload payload, ClientPlayNetworking.Context context) {
			EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.clear();
			payload.map().forEach((key, value) -> EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.put(key.value(), value));
		}
	}
}
