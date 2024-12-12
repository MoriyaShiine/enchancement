/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
	private int enchancement$singleLevelMode(int original) {
		if (ModConfig.singleLevelMode) {
			Enchantment enchantment = (Enchantment) (Object) this;
			if (!EnchancementUtil.ORIGINAL_MAX_LEVELS.containsKey(enchantment)) {
				EnchancementUtil.ORIGINAL_MAX_LEVELS.put(enchantment, original);
			}
			return 1;
		}
		return original;
	}
}
