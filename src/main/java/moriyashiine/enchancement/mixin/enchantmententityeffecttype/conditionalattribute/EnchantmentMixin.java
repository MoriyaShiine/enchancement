/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.conditionalattribute;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "stopLocationBasedEffects", at = @At("HEAD"))
	private void enchancement$conditionalAttribute(int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity, CallbackInfo ci) {
		ModEntityComponents.CONDITIONAL_ATTRIBUTES.get(entity).markRemoved();
	}
}
