/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.payload.SyncVelocityPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

public class SyncVelocitiesEvent implements ClientTickEvents.StartWorldTick {
	@Override
	public void onStartTick(ClientWorld world) {
		if (ModConfig.rebalanceProjectiles) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && player.isPartOfGame()) {
				SyncVelocityPayload.send(player.getVelocity());
			}
		}
	}
}
