/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SyncOriginalMaxLevelsPayload(
		Map<RegistryEntry<Enchantment>, Integer> map) implements CustomPayload {
	public static final Id<SyncOriginalMaxLevelsPayload> ID = new Id<>(Enchancement.id("sync_original_max_levels"));
	public static final PacketCodec<RegistryByteBuf, SyncOriginalMaxLevelsPayload> CODEC = PacketCodec.tuple(PacketCodecs.map(ConcurrentHashMap::new, PacketCodecs.registryEntry(RegistryKeys.ENCHANTMENT), PacketCodecs.VAR_INT), SyncOriginalMaxLevelsPayload::map, SyncOriginalMaxLevelsPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player) {
		Map<RegistryEntry<Enchantment>, Integer> map = new HashMap<>();
		Registry<Enchantment> enchantmentRegistry = player.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
		EnchancementUtil.ORIGINAL_MAX_LEVELS.forEach((key, value) -> map.put(enchantmentRegistry.getOrThrow(key), value));
		ServerPlayNetworking.send(player, new SyncOriginalMaxLevelsPayload(map));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncOriginalMaxLevelsPayload> {
		@Override
		public void receive(SyncOriginalMaxLevelsPayload payload, ClientPlayNetworking.Context context) {
			payload.map().forEach((key, value) -> EnchancementUtil.ORIGINAL_MAX_LEVELS.put(key.getKey().orElseThrow(), value));
		}
	}
}
