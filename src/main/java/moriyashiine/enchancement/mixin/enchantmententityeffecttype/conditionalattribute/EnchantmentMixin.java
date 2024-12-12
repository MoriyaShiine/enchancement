/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.conditionalattribute;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "removeLocationBasedEffects", at = @At("HEAD"))
	private void enchancement$conditionalAttribute(int level, EnchantmentEffectContext context, LivingEntity user, CallbackInfo ci) {
		ModEntityComponents.CONDITIONAL_ATTRIBUTES.get(user).markRemoved();
	}
}
