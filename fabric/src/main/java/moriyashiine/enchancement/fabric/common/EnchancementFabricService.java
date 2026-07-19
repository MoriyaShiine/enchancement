package moriyashiine.enchancement.fabric.common;

import com.google.auto.service.AutoService;
import moriyashiine.enchancement.client.gui.hud.BrimstoneHudElement;
import moriyashiine.enchancement.common.EnchancementService;
import squeek.appleskin.api.event.HUDOverlayEvent;

@AutoService(EnchancementService.class)
public class EnchancementFabricService implements EnchancementService {
	@Override
	public void initAppleSkinIntegration() {
		HUDOverlayEvent.HealthRestored.EVENT.register(healthRestored -> healthRestored.isCanceled = BrimstoneHudElement.forcedHeight != -1);
	}
}
