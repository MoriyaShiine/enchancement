/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.integration.appleskin;

import moriyashiine.enchancement.client.integration.appleskin.event.BrimstoneAppleskinEvent;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class ModAppleskinIntegration implements AppleSkinApi {
	@Override
	public void registerEvents() {
		HUDOverlayEvent.HealthRestored.EVENT.register(new BrimstoneAppleskinEvent());
	}
}
