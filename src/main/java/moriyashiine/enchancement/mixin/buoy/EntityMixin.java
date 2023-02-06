/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.buoy;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract void onLanding();

	@Inject(method = "onBubbleColumnCollision", at = @At("HEAD"), cancellable = true)
	private void enchancement$buoy(boolean drag, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, Entity.class.cast(this))) {
			onLanding();
			ci.cancel();
		}
	}
}
