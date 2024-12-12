/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentLevelEntry.class)
public class EnchantmentLevelEntryMixin {
	@ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
	private static int enchancement$singleLevelMode(int value) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return value;
	}
}
