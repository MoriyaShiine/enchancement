/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.client.payload.SyncEnchantingMaterialMapPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class SyncEnchantingMaterialMapEvent {
	public static boolean shouldSend = false;

	public static class Join implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			SyncEnchantingMaterialMapPayload.send(handler.getPlayer());
		}
	}

	public static class Tick implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer server) {
			if (shouldSend) {
				shouldSend = false;
				PlayerLookup.all(server).forEach(SyncEnchantingMaterialMapPayload::send);
			}
		}
	}
}
