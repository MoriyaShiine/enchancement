/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.component.entity.DelayComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
	private ParticleEffect enchancement$delay(ParticleEffect value) {
		DelayComponent delayComponent = ModEntityComponents.DELAY.getNullable(this);
		if (delayComponent != null && delayComponent.shouldChangeParticles()) {
			return ParticleTypes.ENCHANTED_HIT;
		}
		return value;
	}

	@Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", shift = At.Shift.BEFORE))
	private void enchancement$delay(EntityHitResult entityHitResult, CallbackInfo ci) {
		ModEntityComponents.DELAY.maybeGet(this).ifPresent(delayComponent -> {
			if (delayComponent.alwaysHurt() && entityHitResult.getEntity() instanceof LivingEntity living) {
				living.timeUntilRegen = 0;
			}
		});
	}
}
