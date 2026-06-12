/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceeffects;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.effect.HealOrHarmMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HealOrHarmMobEffect.class)
public class HealOrHarmMobEffectMixin {
	@ModifyArg(method = "applyInstantaneousEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceEffectsInstantHealth(float heal) {
		if (EnchancementConfig.rebalanceEffects) {
			return heal * 3 / 4F;
		}
		return heal;
	}

	@ModifyArg(method = "applyInstantaneousEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private float enchancement$rebalanceEffectsInstantDamage(float damage) {
		if (EnchancementConfig.rebalanceEffects) {
			return damage / 2F;
		}
		return damage;
	}

	@ModifyArg(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalanceEffectsUpdateHealth(float heal) {
		if (EnchancementConfig.rebalanceEffects) {
			return heal * 3 / 4F;
		}
		return heal;
	}

	@ModifyArg(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private float enchancement$rebalanceEffectsUpdateDamage(float damage) {
		if (EnchancementConfig.rebalanceEffects) {
			return damage / 2F;
		}
		return damage;
	}
}
