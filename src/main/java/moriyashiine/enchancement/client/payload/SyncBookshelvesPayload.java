/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.event.internal.SyncBookshelvesEvent;
import moriyashiine.enchancement.client.gui.screens.inventory.ModEnchantmentScreen;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashSet;
import java.util.Set;

public record SyncBookshelvesPayload(Set<Holder<Enchantment>> chiseledEnchantments, int bookshelfCount) implements CustomPacketPayload {
	public static final Type<SyncBookshelvesPayload> TYPE = new Type<>(Enchancement.id("sync_bookshelves"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncBookshelvesPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT)), SyncBookshelvesPayload::chiseledEnchantments,
			ByteBufCodecs.VAR_INT, SyncBookshelvesPayload::bookshelfCount,
			SyncBookshelvesPayload::new);

	@Override
	public Type<SyncBookshelvesPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, Set<Holder<Enchantment>> chiseledEnchantments, int bookshelfCount) {
		ServerPlayNetworking.send(player, new SyncBookshelvesPayload(chiseledEnchantments, bookshelfCount));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncBookshelvesPayload> {
		@Override
		public void receive(SyncBookshelvesPayload payload, ClientPlayNetworking.Context context) {
			SyncBookshelvesEvent.CHISELED_ENCHANTMENTS = payload.chiseledEnchantments();
			ModEnchantmentScreen.bookshelfCount = payload.bookshelfCount();
		}
	}
}
