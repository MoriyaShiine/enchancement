/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DelayedLaunchComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
	private ParticleOptions enchancement$delayedLaunch(ParticleOptions value) {
		DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.getNullable(this);
		if (delayedLaunchComponent != null && delayedLaunchComponent.shouldChangeParticles()) {
			return ParticleTypes.ENCHANTED_HIT;
		}
		return value;
	}
}
