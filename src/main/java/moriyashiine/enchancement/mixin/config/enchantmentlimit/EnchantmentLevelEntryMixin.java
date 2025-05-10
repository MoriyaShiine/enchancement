/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentLevelEntry.class)
public class EnchantmentLevelEntryMixin {
	@ModifyReturnValue(method = "getWeight", at = @At("RETURN"))
	private int enchancement$enchantmentLimit(int original) {
		if (ModConfig.enchantmentLimit > 0) {
			return 1;
		}
		return original;
	}
}
