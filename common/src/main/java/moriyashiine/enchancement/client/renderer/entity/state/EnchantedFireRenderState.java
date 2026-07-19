package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class EnchantedFireRenderState {
	public static final RenderStateDataKey<EnchantedFireRenderState> KEY = RenderStateDataKey.create(() -> "enchanted fire");

	public boolean renderEnchantedFire = false;

	public interface Submit {
		boolean enchancement$renderEnchantedFire();

		void enchancement$setRenderEnchantedFire(boolean renderEnchantedFire);
	}
}
