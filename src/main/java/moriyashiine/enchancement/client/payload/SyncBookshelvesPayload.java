/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.event.internal.SyncBookshelvesEvent;
import moriyashiine.enchancement.client.gui.screen.ingame.EnchantingTableScreen;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public record SyncBookshelvesPayload(Set<RegistryEntry<Enchantment>> chiseledEnchantments,
									 int bookshelfCount) implements CustomPayload {
	public static final CustomPayload.Id<SyncBookshelvesPayload> ID = new Id<>(Enchancement.id("sync_bookshelves"));
	public static final PacketCodec<RegistryByteBuf, SyncBookshelvesPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.collection(HashSet::new, PacketCodecs.registryEntry(RegistryKeys.ENCHANTMENT)), SyncBookshelvesPayload::chiseledEnchantments,
			PacketCodecs.VAR_INT, SyncBookshelvesPayload::bookshelfCount,
			SyncBookshelvesPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, Set<RegistryEntry<Enchantment>> chiseledEnchantments, int bookshelfCount) {
		ServerPlayNetworking.send(player, new SyncBookshelvesPayload(chiseledEnchantments, bookshelfCount));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncBookshelvesPayload> {
		@Override
		public void receive(SyncBookshelvesPayload payload, ClientPlayNetworking.Context context) {
			SyncBookshelvesEvent.CHISELED_ENCHANTMENTS = payload.chiseledEnchantments();
			EnchantingTableScreen.bookshelfCount = payload.bookshelfCount();
		}
	}
}
