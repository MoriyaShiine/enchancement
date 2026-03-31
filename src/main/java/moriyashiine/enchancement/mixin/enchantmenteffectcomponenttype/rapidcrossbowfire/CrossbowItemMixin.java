/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Inject(method = "getChargeDuration", at = @At("HEAD"), cancellable = true)
	private static void enchancement$rapidCrossbowFire(ItemStack crossbow, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (EnchantmentHelper.has(crossbow, ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			cir.setReturnValue(0);
		}
	}
}
