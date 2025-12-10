/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.integration.appleskin.event;

import moriyashiine.enchancement.client.hud.BrimstoneHudElement;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.handler.EventHandler;

public class BrimstoneAppleskinEvent implements EventHandler<HUDOverlayEvent.HealthRestored> {
	@Override
	public void interact(HUDOverlayEvent.HealthRestored healthRestored) {
		healthRestored.isCanceled = BrimstoneHudElement.forcedHeight != -1;
	}
}
