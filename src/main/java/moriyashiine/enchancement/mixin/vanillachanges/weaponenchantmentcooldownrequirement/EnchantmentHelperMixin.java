/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.weaponenchantmentcooldownrequirement;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
	private static void enchancement$weaponEnchantmentCooldownRequirement(LivingEntity user, Entity target, CallbackInfo ci) {
		if (EnchancementUtil.shouldCancelTargetDamagedEnchantments) {
			EnchancementUtil.shouldCancelTargetDamagedEnchantments = false;
			ci.cancel();
		}
	}
}
