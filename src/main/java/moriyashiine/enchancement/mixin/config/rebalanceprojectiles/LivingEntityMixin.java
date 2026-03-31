/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.DelayedLaunchComponent;
import moriyashiine.enchancement.common.component.entity.ProjectileTimerComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
	private float enchancement$rebalanceProjectiles(float damage, ServerLevel level, DamageSource source) {
		if (source.getDirectEntity() instanceof Projectile projectile) {
			boolean bypass = ModConfig.rebalanceProjectiles;
			if (!bypass) {
				DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.getNullable(projectile);
				if (delayedLaunchComponent != null && delayedLaunchComponent.isEnabled()) {
					bypass = true;
				}
			}
			if (bypass && !projectile.is(ModEntityTypeTags.BYPASSES_DECREASING_DAMAGE)) {
				ProjectileTimerComponent projectileTimerComponent = ModEntityComponents.PROJECTILE_TIMER.get(this);
				projectileTimerComponent.incrementTimesHit();
				projectileTimerComponent.markAsHit();
				boolean aboveOrEqualToOne = damage >= 1;
				damage *= (float) Math.pow(source.is(DamageTypeTags.BYPASSES_ARMOR) ? 0.2 : 0.8, projectileTimerComponent.getTimesHit() - 1);
				if (aboveOrEqualToOne) {
					damage = Math.max(1, damage);
				}
			}
		}
		return damage;
	}

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 3))
	private boolean enchancement$rebalanceProjectiles(boolean value, ServerLevel level, DamageSource source) {
		if (ModConfig.rebalanceProjectiles && source.getDirectEntity() instanceof Projectile) {
			return true;
		}
		return value;
	}

	@Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;indicateDamage(DD)V"))
	private void enchancement$rebalanceProjectiles(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.rebalanceProjectiles && source.getDirectEntity() instanceof Projectile) {
			setDeltaMovement(0, Math.min(0, getDeltaMovement().y()), 0);
			hurtMarked = true;
		}
	}
}
