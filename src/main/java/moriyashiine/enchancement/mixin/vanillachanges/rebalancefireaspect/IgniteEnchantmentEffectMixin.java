/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.rebalancefireaspect;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.effect.entity.IgniteEnchantmentEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IgniteEnchantmentEffect.class)
public class IgniteEnchantmentEffectMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(F)V"))
	private float enchancement$rebalanceFireAspect(float value) {
		if (ModConfig.rebalanceFireAspect) {
			return value * 3 / 4;
		}
		return value;
	}
}
