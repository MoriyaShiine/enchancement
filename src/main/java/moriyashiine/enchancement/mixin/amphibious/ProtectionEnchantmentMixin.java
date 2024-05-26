/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.amphibious;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
	@ModifyVariable(method = "transformFireDuration", at = @At("HEAD"), argsOnly = true)
	private static int enchancement$amphibious(int value, LivingEntity living) {
		if (value > 0) {
			int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, living);
			if (level > 0) {
				return Math.max(0, MathHelper.ceil(value * (1 - level * 0.25F)));
			}
		}
		return value;
	}
}
