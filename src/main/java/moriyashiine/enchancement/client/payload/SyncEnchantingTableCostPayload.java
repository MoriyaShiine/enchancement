/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.gui.screens.inventory.ModEnchantmentScreen;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SyncEnchantingTableCostPayload(int cost) implements CustomPacketPayload {
	public static final Type<SyncEnchantingTableCostPayload> TYPE = new Type<>(Enchancement.id("sync_enchanting_table_cost"));
	public static final StreamCodec<FriendlyByteBuf, SyncEnchantingTableCostPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SyncEnchantingTableCostPayload::cost,
			SyncEnchantingTableCostPayload::new);

	@Override
	public Type<SyncEnchantingTableCostPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, int cost) {
		ServerPlayNetworking.send(player, new SyncEnchantingTableCostPayload(cost));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingTableCostPayload> {
		@Override
		public void receive(SyncEnchantingTableCostPayload payload, ClientPlayNetworking.Context context) {
			if (context.client().screen instanceof ModEnchantmentScreen modEnchantmentScreen) {
				modEnchantmentScreen.getMenu().setCost(payload.cost());
			}
		}
	}
}
