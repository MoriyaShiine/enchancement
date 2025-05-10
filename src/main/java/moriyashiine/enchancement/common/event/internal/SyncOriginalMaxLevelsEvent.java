/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.client.payload.SyncOriginalMaxLevelsPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class SyncOriginalMaxLevelsEvent {
	public static boolean updatingMap = false;

	public static class Join implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			SyncOriginalMaxLevelsPayload.send(handler.getPlayer());
		}
	}

	public static class ServerStarted implements ServerLifecycleEvents.ServerStarted {
		@Override
		public void onServerStarted(MinecraftServer server) {
			updatingMap = true;
			EnchancementUtil.ORIGINAL_MAX_LEVELS.clear();
			server.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).forEach(enchantment -> EnchancementUtil.ORIGINAL_MAX_LEVELS.put(enchantment, enchantment.getMaxLevel()));
			updatingMap = false;
		}
	}
}
