/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.screen.EnchantingTableScreen;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record SyncEnchantingTableBookshelfCountPayload(int bookshelfCount) implements CustomPayload {
	public static final CustomPayload.Id<SyncEnchantingTableBookshelfCountPayload> ID = CustomPayload.id(Enchancement.id("sync_enchanting_table_bookshelf_count").toString());
	public static final PacketCodec<PacketByteBuf, SyncEnchantingTableBookshelfCountPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, SyncEnchantingTableBookshelfCountPayload::bookshelfCount, SyncEnchantingTableBookshelfCountPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int bookshelfCount) {
		ServerPlayNetworking.send(player, new SyncEnchantingTableBookshelfCountPayload(bookshelfCount));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingTableBookshelfCountPayload> {
		@Override
		public void receive(SyncEnchantingTableBookshelfCountPayload payload, ClientPlayNetworking.Context context) {
			EnchantingTableScreen.bookshelfCount = payload.bookshelfCount();
		}
	}
}
