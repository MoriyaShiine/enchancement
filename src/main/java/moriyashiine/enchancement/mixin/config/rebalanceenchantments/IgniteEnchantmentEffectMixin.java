/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.effect.entity.IgniteEnchantmentEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IgniteEnchantmentEffect.class)
public class IgniteEnchantmentEffectMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(F)V"))
	private float enchancement$rebalanceEnchantments(float value) {
		if (ModConfig.rebalanceEnchantments) {
			return value * 3 / 4;
		}
		return value;
	}
}
