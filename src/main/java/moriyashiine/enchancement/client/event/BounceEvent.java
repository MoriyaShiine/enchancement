/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.BounceComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.SyncInvertedBounceStatusPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class BounceEvent implements ClientTickEvents.EndWorldTick {
	@Override
	public void onEndTick(ClientWorld world) {
		BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(MinecraftClient.getInstance().player);
		boolean current = bounceComponent.hasInvertedBounce();
		if (current != ModConfig.invertedBounce) {
			bounceComponent.setInvertedBounce(ModConfig.invertedBounce);
			SyncInvertedBounceStatusPayload.send(ModConfig.invertedBounce);
		}
	}
}
