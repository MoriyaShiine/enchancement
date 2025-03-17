/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.integration.appleskin;

import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.BrimstoneClientEvent;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.handler.EventHandler;

public class BrimstoneAppleskinEvent implements EventHandler<HUDOverlayEvent.HealthRestored> {
	@Override
	public void interact(HUDOverlayEvent.HealthRestored healthRestored) {
		healthRestored.isCanceled = BrimstoneClientEvent.forcedHeight != -1;
	}
}
