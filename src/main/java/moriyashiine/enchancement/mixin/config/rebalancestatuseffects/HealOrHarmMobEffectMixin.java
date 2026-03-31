/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.effect.HealOrHarmMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HealOrHarmMobEffect.class)
public class HealOrHarmMobEffectMixin {
	@ModifyArg(method = "applyInstantenousEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceStatusEffectsInstantHealth(float heal) {
		if (ModConfig.rebalanceStatusEffects) {
			return heal * 3 / 4F;
		}
		return heal;
	}

	@ModifyArg(method = "applyInstantenousEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private float enchancement$rebalanceStatusEffectsInstantDamage(float damage) {
		if (ModConfig.rebalanceStatusEffects) {
			return damage / 2F;
		}
		return damage;
	}

	@ModifyArg(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceStatusEffectsUpdateHealth(float heal) {
		if (ModConfig.rebalanceStatusEffects) {
			return heal * 3 / 4F;
		}
		return heal;
	}

	@ModifyArg(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private float enchancement$rebalanceStatusEffectsUpdateDamage(float damage) {
		if (ModConfig.rebalanceStatusEffects) {
			return damage / 2F;
		}
		return damage;
	}
}
