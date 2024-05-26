/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FabricItemStack.class)
public interface FabricItemStackMixin {
	@ModifyReturnValue(method = "canBeEnchantedWith", at = @At("RETURN"))
	private boolean enchancement$disableDisallowedEnchantmentsSet(boolean original, Enchantment enchantment) {
		if (!enchantment.isEnabled(FeatureFlags.DEFAULT_ENABLED_FEATURES)) {
			return false;
		}
		return original;
	}
}
