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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;blockedByShield(Lnet/minecraft/entity/damage/DamageSource;)Z"), argsOnly = true)
	private float enchancement$rebalanceProjectiles(float value, ServerWorld world, DamageSource source) {
		if (source.getSource() instanceof ProjectileEntity projectile) {
			boolean bypass = ModConfig.rebalanceProjectiles;
			if (!bypass) {
				DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.getNullable(projectile);
				if (delayedLaunchComponent != null && delayedLaunchComponent.isEnabled()) {
					bypass = true;
				}
			}
			if (bypass && !projectile.getType().isIn(ModEntityTypeTags.BYPASSES_DECREASING_DAMAGE)) {
				ProjectileTimerComponent projectileTimerComponent = ModEntityComponents.PROJECTILE_TIMER.get(this);
				projectileTimerComponent.incrementTimesHit();
				projectileTimerComponent.markAsHit();
				boolean aboveOrEqualToOne = value >= 1;
				value *= (float) Math.pow(source.isIn(DamageTypeTags.BYPASSES_ARMOR) ? 0.2 : 0.8, projectileTimerComponent.getTimesHit() - 1);
				if (aboveOrEqualToOne) {
					value = Math.max(1, value);
				}
			}
		}
		return value;
	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 4))
	private boolean enchancement$rebalanceProjectiles(boolean value, ServerWorld world, DamageSource source) {
		if (ModConfig.rebalanceProjectiles && source.getSource() instanceof ProjectileEntity) {
			return true;
		}
		return value;
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tiltScreen(DD)V"))
	private void enchancement$rebalanceProjectiles(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.rebalanceProjectiles && source.getSource() instanceof ProjectileEntity) {
			setVelocity(0, Math.min(0, getVelocity().getY()), 0);
			velocityModified = true;
		}
	}
}
