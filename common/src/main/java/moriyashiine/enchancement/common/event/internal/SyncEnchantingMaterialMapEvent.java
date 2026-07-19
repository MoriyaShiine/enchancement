package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.client.payload.SyncEnchantingMaterialMapPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class SyncEnchantingMaterialMapEvent {
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register(new Join());
		ServerTickEvents.END_SERVER_TICK.register(new Tick());
	}

	public static boolean shouldSend = false;

	private static class Join implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
			SyncEnchantingMaterialMapPayload.send(listener.getPlayer());
		}
	}

	private static class Tick implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer server) {
			if (shouldSend) {
				shouldSend = false;
				PlayerLookup.all(server).forEach(SyncEnchantingMaterialMapPayload::send);
			}
		}
	}
}
