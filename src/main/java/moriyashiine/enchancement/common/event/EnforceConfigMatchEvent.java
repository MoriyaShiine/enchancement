/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.client.payload.EnforceConfigMatchPayload;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class EnforceConfigMatchEvent {
	public static class Join implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			EnforceConfigMatchPayload.send(handler.getPlayer(), ModConfig.encode());
		}
	}

	public static class Tick implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer server) {
			if (server.getOverworld().getTime() % 100 == 0) {
				for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
					EnforceConfigMatchPayload.send(player, ModConfig.encode());
				}
			}
		}
	}
}
