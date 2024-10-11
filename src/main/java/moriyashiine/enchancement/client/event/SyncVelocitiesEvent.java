/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.payload.SyncVelocityPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class SyncVelocitiesEvent implements ClientTickEvents.StartWorldTick {
	@Override
	public void onStartTick(ClientWorld world) {
		if (ModConfig.rebalanceProjectiles) {
			SyncVelocityPayload.send(MinecraftClient.getInstance().player.getVelocity());
		}
	}
}
