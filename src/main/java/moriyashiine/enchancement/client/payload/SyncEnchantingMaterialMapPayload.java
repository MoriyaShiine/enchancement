/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.EnchantingMaterial;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record SyncEnchantingMaterialMapPayload(
		Map<Holder<Item>, EnchantingMaterial> map) implements CustomPacketPayload {
	public static final Type<SyncEnchantingMaterialMapPayload> TYPE = new Type<>(Enchancement.id("sync_enchanting_material_map"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncEnchantingMaterialMapPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.holderRegistry(Registries.ITEM), EnchantingMaterial.STREAM_CODEC), SyncEnchantingMaterialMapPayload::map,
			SyncEnchantingMaterialMapPayload::new);

	@Override
	public Type<SyncEnchantingMaterialMapPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player) {
		Map<Holder<Item>, EnchantingMaterial> map = new HashMap<>();
		EnchantingMaterial.MATERIAL_MAP.forEach((key, value) -> map.put(BuiltInRegistries.ITEM.wrapAsHolder(key), value));
		ServerPlayNetworking.send(player, new SyncEnchantingMaterialMapPayload(map));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingMaterialMapPayload> {
		@Override
		public void receive(SyncEnchantingMaterialMapPayload payload, ClientPlayNetworking.Context context) {
			EnchantingMaterial.MATERIAL_MAP.clear();
			payload.map().forEach((holder, value) -> EnchantingMaterial.MATERIAL_MAP.put(holder.value(), value));
		}
	}
}
