/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.scooping;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getAttackDamage", at = @At("RETURN"))
	private static float enchancement$scooping(float original, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.SCOOPING, stack)) {
			return original + 1;
		}
		return original;
	}

	@ModifyReturnValue(method = "getLooting", at = @At("RETURN"))
	private static int enchancement$scooping(int original, LivingEntity entity) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.SCOOPING, entity.getMainHandStack())) {
			return EnchancementUtil.alterLevel(entity.getMainHandStack(), Enchantments.LOOTING, 2);
		}
		return original;
	}
}
