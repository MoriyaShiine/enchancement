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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getAttackDamage", at = @At("RETURN"))
	private static float enchancement$scooping(float original, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.SCOOPING, stack)) {
			return original + 1;
		}
		return original;
	}

	@Inject(method = "getLooting", at = @At("HEAD"), cancellable = true)
	private static void enchancement$scooping(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
		int level = EnchantmentHelper.getLevel(ModEnchantments.SCOOPING, entity.getMainHandStack());
		if (level > 0) {
			cir.setReturnValue(EnchancementUtil.getModifiedMaxLevel(entity.getMainHandStack(), Enchantments.LOOTING.getMaxLevel() + 2));
		}
	}
}
