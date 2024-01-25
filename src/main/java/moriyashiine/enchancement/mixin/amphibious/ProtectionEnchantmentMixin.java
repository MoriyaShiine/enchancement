/*
 * All Rights Reserved (c) MoriyaShiine
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
				return MathHelper.ceil(value * (level == 1 ? 0.75F : 0.5F));
			}
		}
		return value;
	}
}
