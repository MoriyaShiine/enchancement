/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.amphibious;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
	@ModifyVariable(method = "transformFireDuration", at = @At("HEAD"), argsOnly = true)
	private static int enchancement$amphibious(int value, LivingEntity living) {
		if (value > 0 && EnchancementUtil.hasEnchantment(ModEnchantments.AMPHIBIOUS, living)) {
			return Math.max(1, value / 2);
		}
		return value;
	}
}
