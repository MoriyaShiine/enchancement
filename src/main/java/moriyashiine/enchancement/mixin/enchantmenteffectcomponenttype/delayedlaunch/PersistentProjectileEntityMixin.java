/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import moriyashiine.enchancement.common.component.entity.DelayedLaunchComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
	private ParticleEffect enchancement$delayedLaunch(ParticleEffect value) {
		DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.getNullable(this);
		if (delayedLaunchComponent != null && delayedLaunchComponent.shouldChangeParticles()) {
			return ParticleTypes.ENCHANTED_HIT;
		}
		return value;
	}
}
