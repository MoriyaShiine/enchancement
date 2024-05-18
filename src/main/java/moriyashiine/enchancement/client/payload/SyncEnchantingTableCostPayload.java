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

public record SyncEnchantingTableCostPayload(int cost) implements CustomPayload {
	public static final CustomPayload.Id<SyncEnchantingTableCostPayload> ID = CustomPayload.id(Enchancement.id("sync_enchanting_table_cost").toString());
	public static final PacketCodec<PacketByteBuf, SyncEnchantingTableCostPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, SyncEnchantingTableCostPayload::cost, SyncEnchantingTableCostPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int cost) {
		ServerPlayNetworking.send(player, new SyncEnchantingTableCostPayload(cost));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SyncEnchantingTableCostPayload> {
		@Override
		public void receive(SyncEnchantingTableCostPayload payload, ClientPlayNetworking.Context context) {
			if (context.client().currentScreen instanceof EnchantingTableScreen enchantingTableScreen) {
				enchantingTableScreen.getScreenHandler().setCost(payload.cost());
			}
		}
	}
}
