package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import moriyashiine.enchancement.client.renderer.entity.state.EnchantedFireRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SubmitNodeStorage.FlameSubmit.class)
public class FlameSubmitMixin implements EnchantedFireRenderState.Submit {
	@Unique
	private boolean renderEnchantedFire = false;

	@Override
	public boolean enchancement$renderEnchantedFire() {
		return renderEnchantedFire;
	}

	@Override
	public void enchancement$setRenderEnchantedFire(boolean renderEnchantedFire) {
		this.renderEnchantedFire = renderEnchantedFire;
	}
}
