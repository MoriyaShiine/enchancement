/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.HealOrHarmMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HealOrHarmMobEffect.class)
public class HealOrHarmMobEffectMixin {
	@WrapOperation(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
	private void enchancement$enchancement$rebalanceProjectiles(LivingEntity instance, float heal, Operation<Void> original) {
		if (ModConfig.rebalanceProjectiles) {
			heal *= (float) Math.pow(0.8, Math.max(0, ModEntityComponents.PROJECTILE_TIMER.get(instance).getTimesHit() - 1));
		}
		original.call(instance, heal);
	}

	@WrapOperation(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private boolean enchancement$enchancement$rebalanceProjectiles(LivingEntity instance, ServerLevel level, DamageSource source, float damage, Operation<Boolean> original) {
		if (ModConfig.rebalanceProjectiles) {
			damage *= (float) Math.pow(0.8, Math.max(0, ModEntityComponents.PROJECTILE_TIMER.get(instance).getTimesHit() - 1));
		}
		return original.call(instance, level, source, damage);
	}
}
