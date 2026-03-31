/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.BounceComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.SyncInvertedBounceStatusPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class BounceClientEvent implements ClientTickEvents.EndLevelTick {
	@Override
	public void onEndTick(ClientLevel level) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(player);
			boolean current = bounceComponent.hasInvertedBounce();
			if (current != ModConfig.invertedBounce) {
				bounceComponent.setInvertedBounce(ModConfig.invertedBounce);
				SyncInvertedBounceStatusPayload.send(ModConfig.invertedBounce);
			}
		}
	}
}
