/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToggleableFeature.class)
public interface EnchantmentPropertiesMixin {
	@ModifyReturnValue(method = "isEnabled", at = @At("RETURN"))
	private boolean enchancement$disableDisallowedEnchantments(boolean original) {
		if ((ToggleableFeature) this instanceof Enchantment enchantment && !EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			return false;
		}
		return original;
	}
}
