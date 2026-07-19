/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.entity.config.ProjectileTimerComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DelayedLaunchComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.tag.EnchancementEntityTypeTags;
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
			boolean enabled = EnchancementConfig.rebalanceProjectiles;
			if (!enabled) {
				DelayedLaunchComponent delayedLaunch = EnchancementEntityComponents.DELAYED_LAUNCH.getNullable(projectile);
				if (delayedLaunch != null && delayedLaunch.wasEverEnabled()) {
					enabled = true;
				}
			}
			if (enabled && !projectile.is(EnchancementEntityTypeTags.BYPASSES_DECREASING_DAMAGE)) {
				ProjectileTimerComponent projectileTimer = EnchancementEntityComponents.PROJECTILE_TIMER.get(this);
				projectileTimer.hit();
				boolean aboveOrEqualToOne = damage >= 1;
				damage *= (float) Math.pow(source.is(DamageTypeTags.BYPASSES_ARMOR) ? 0.2 : 0.8, projectileTimer.getTimesHit() - 1);
				if (aboveOrEqualToOne) {
					damage = Math.max(1, damage);
				}
			}
		}
		return damage;
	}

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 3))
	private boolean enchancement$rebalanceProjectiles(boolean value, ServerLevel level, DamageSource source) {
		if (!value && source.getDirectEntity() instanceof Projectile projectile) {
			boolean enabled = EnchancementConfig.rebalanceProjectiles;
			if (!enabled) {
				DelayedLaunchComponent delayedLaunch = EnchancementEntityComponents.DELAYED_LAUNCH.getNullable(projectile);
				if (delayedLaunch != null && delayedLaunch.wasEverEnabled()) {
					enabled = true;
				}
			}
			return enabled;
		}
		return value;
	}

	@Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;indicateDamage(DD)V"))
	private void enchancement$rebalanceProjectiles(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementConfig.rebalanceProjectiles && source.getDirectEntity() instanceof Projectile) {
			setDeltaMovement(0, Math.min(0, getDeltaMovement().y()), 0);
			hurtMarked = true;
		}
	}
}
