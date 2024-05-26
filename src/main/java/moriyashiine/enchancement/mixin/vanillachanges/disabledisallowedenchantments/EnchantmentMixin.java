/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements ToggleableFeature {
	@ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
	private boolean enchancement$disableDisallowedEnchantmentsSet(boolean original) {
		if (!isEnabled(FeatureFlags.DEFAULT_ENABLED_FEATURES)) {
			return false;
		}
		return original;
	}
}
