/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.InstantHealthOrDamageStatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InstantHealthOrDamageStatusEffect.class)
public class InstantHealthOrDamageStatusEffectMixin {
	@WrapOperation(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private void enchancement$enchancement$rebalanceProjectiles(LivingEntity instance, float amount, Operation<Void> original) {
		if (ModConfig.rebalanceProjectiles) {
			amount *= (float) Math.pow(0.8, Math.max(0, ModEntityComponents.PROJECTILE_TIMER.get(instance).getTimesHit() - 1));
		}
		original.call(instance, amount);
	}

	@WrapOperation(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private boolean enchancement$enchancement$rebalanceProjectiles(LivingEntity instance, DamageSource source, float amount, Operation<Boolean> original) {
		if (ModConfig.rebalanceProjectiles) {
			amount *= (float) Math.pow(0.8, Math.max(0, ModEntityComponents.PROJECTILE_TIMER.get(instance).getTimesHit() - 1));
		}
		return original.call(instance, source, amount);
	}
}
