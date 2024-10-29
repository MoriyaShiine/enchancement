/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.InstantHealthOrDamageStatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InstantHealthOrDamageStatusEffect.class)
public class InstantHealthOrDamageStatusEffectMixin {
	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceStatusEffectsInstantHealth(float value) {
		if (ModConfig.rebalanceStatusEffects) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$rebalanceStatusEffectsInstantDamage(float value) {
		if (ModConfig.rebalanceStatusEffects) {
			return value / 2F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceStatusEffectsUpdateHealth(float value) {
		if (ModConfig.rebalanceStatusEffects) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$rebalanceStatusEffectsUpdateDamage(float value) {
		if (ModConfig.rebalanceStatusEffects) {
			return value / 2F;
		}
		return value;
	}
}
