/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantmentsComponent.Builder.class)
public class ItemEnchantmentsComponentBuilderMixin {
	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsSet(Enchantment enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to set a null enchantment");
			ci.cancel();
		} else if (!enchantment.isEnabled(FeatureFlags.DEFAULT_ENABLED_FEATURES)) {
			Enchancement.LOGGER.warn("Attempted to set a disabled enchantment {}", enchantment.getTranslationKey());
			ci.cancel();
		}
	}

	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsAdd(Enchantment enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to add a null enchantment");
			ci.cancel();
		} else if (!enchantment.isEnabled(FeatureFlags.DEFAULT_ENABLED_FEATURES)) {
			Enchancement.LOGGER.warn("Attempted to add a disabled enchantment {}", enchantment.getTranslationKey());
			ci.cancel();
		}
	}
}
