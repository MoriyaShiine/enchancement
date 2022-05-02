/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.component.entity.DelayComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
}
