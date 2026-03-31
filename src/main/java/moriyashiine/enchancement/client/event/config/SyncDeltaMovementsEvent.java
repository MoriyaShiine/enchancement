/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.payload.SyncDeltaMovementPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class SyncDeltaMovementsEvent implements ClientTickEvents.StartLevelTick {
	@Override
	public void onStartTick(ClientLevel level) {
		if (ModConfig.rebalanceProjectiles) {
			Player player = Minecraft.getInstance().player;
			if (player != null && player.slib$exists()) {
				SyncDeltaMovementPayload.send(player.getDeltaMovement());
			}
		}
	}
}
