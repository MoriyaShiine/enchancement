package moriyashiine.enchancement.neoforge.common;

import com.google.auto.service.AutoService;
import moriyashiine.enchancement.client.gui.hud.BrimstoneHudElement;
import moriyashiine.enchancement.common.EnchancementService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import squeek.appleskin.api.event.HUDOverlayEvent;

@AutoService(EnchancementService.class)
public class EnchancementNeoForgeService implements EnchancementService {
	@Override
	public void initAppleSkinIntegration() {
		NeoForge.EVENT_BUS.register(new AppleSkinIntegration());
	}

	private static class AppleSkinIntegration {
		@SubscribeEvent
		public void onHealthRestoredEvent(HUDOverlayEvent.HealthRestored event) {
			if (BrimstoneHudElement.forcedHeight != -1) {
				event.setCanceled(true);
			}
		}
	}
}
