/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SyncOriginalMaxLevelsPayload(Map<Holder<Enchantment>, Integer> map) implements CustomPacketPayload {
	public static final Type<SyncOriginalMaxLevelsPayload> TYPE = new Type<>(Enchancement.id("sync_original_max_levels"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncOriginalMaxLevelsPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.map(ConcurrentHashMap::new, ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT), ByteBufCodecs.VAR_INT), SyncOriginalMaxLevelsPayload::map,
			SyncOriginalMaxLevelsPayload::new);

	@Override
	public Type<SyncOriginalMaxLevelsPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player) {
		Map<Holder<Enchantment>, Integer> map = new HashMap<>();
		Registry<Enchantment> enchantmentRegistry = player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		EnchancementUtil.ORIGINAL_MAX_LEVELS.forEach((key, value) -> map.put(enchantmentRegistry.getOrThrow(key), value));
		ServerPlayNetworking.send(player, new SyncOriginalMaxLevelsPayload(map));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncOriginalMaxLevelsPayload> {
		@Override
		public void receive(SyncOriginalMaxLevelsPayload payload, ClientPlayNetworking.Context context) {
			payload.map().forEach((enchantment, level) -> EnchancementUtil.ORIGINAL_MAX_LEVELS.put(enchantment.unwrapKey().orElseThrow(), level));
		}
	}
}
