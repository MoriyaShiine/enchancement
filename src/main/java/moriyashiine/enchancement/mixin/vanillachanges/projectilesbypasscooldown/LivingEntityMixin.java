/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.projectilesbypasscooldown;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.DelayComponent;
import moriyashiine.enchancement.common.component.entity.ProjectileTimerComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), argsOnly = true)
	private float enchancement$projectilesBypassCooldownTimer(float value, DamageSource source) {
		if (source.getSource() instanceof ProjectileEntity projectile) {
			boolean bypass = ModConfig.projectilesBypassCooldown;
			if (!bypass) {
				DelayComponent delayComponent = ModEntityComponents.DELAY.getNullable(projectile);
				if (delayComponent != null && delayComponent.hasDelay()) {
					bypass = true;
				}
			}
			if (bypass && !projectile.getType().isIn(ModTags.EntityTypes.BYPASSES_DECREASING_DAMAGE)) {
				ProjectileTimerComponent projectileTimerComponent = ModEntityComponents.PROJECTILE_TIMER.get(this);
				projectileTimerComponent.incrementTimesHit();
				projectileTimerComponent.markAsHit();
				boolean aboveOrEqualToOne = value >= 1;
				for (int i = 1; i < projectileTimerComponent.getTimesHit(); i++) {
					value *= 0.8F;
				}
				if (aboveOrEqualToOne) {
					value = Math.max(1, value);
				}
			}
		}
		return value;
	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 3))
	private boolean enchancement$projectilesBypassCooldown(boolean value, DamageSource source) {
		if (ModConfig.projectilesBypassCooldown && source.getSource() instanceof ProjectileEntity) {
			return true;
		}
		return value;
	}
}
