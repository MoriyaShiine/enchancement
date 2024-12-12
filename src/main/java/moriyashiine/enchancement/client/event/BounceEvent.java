/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.payload.SyncBounceInvertedStatusPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

public class BounceEvent {
	public static class Disconnect implements ClientPlayConnectionEvents.Disconnect {
		@Override
		public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
			SyncBounceInvertedStatusPayload.ENTITIES_WITH_INVERTED_STATUS.clear();
		}
	}

	public static class Tick implements ClientTickEvents.EndWorldTick {
		@Override
		public void onEndTick(ClientWorld world) {
			SyncBounceInvertedStatusPayload.ENTITIES_WITH_INVERTED_STATUS.removeIf(entity -> entity == null || entity.isRemoved());
			PlayerEntity player = MinecraftClient.getInstance().player;
			boolean current = SyncBounceInvertedStatusPayload.ENTITIES_WITH_INVERTED_STATUS.contains(player);
			if (current != ModConfig.invertedBounce) {
				SyncBounceInvertedStatusPayload.toggle(player, ModConfig.invertedBounce);
				SyncBounceInvertedStatusPayload.send(ModConfig.invertedBounce);
			}
		}
	}
}
