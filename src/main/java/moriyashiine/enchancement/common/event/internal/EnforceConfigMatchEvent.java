/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.client.payload.EnforceConfigMatchPayload;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class EnforceConfigMatchEvent {
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register(new Join());
		ServerTickEvents.END_SERVER_TICK.register(new Tick());
	}

	private static class Join implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
			EnforceConfigMatchPayload.send(listener.getPlayer(), ModConfig.encode());
		}
	}

	private static class Tick implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer server) {
			if (server.overworld().getGameTime() % 100 == 0) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					EnforceConfigMatchPayload.send(player, ModConfig.encode());
				}
			}
		}
	}
}
