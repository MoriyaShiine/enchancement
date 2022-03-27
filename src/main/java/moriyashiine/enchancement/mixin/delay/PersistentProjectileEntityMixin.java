package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.registry.ModComponents;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
	private ParticleEffect enchancement$delayChangeParticle(ParticleEffect value) {
		if (PersistentProjectileEntity.class.cast(this) instanceof ArrowEntity arrow && ModComponents.DELAY.get(arrow).shouldChangeParticles()) {
			return ParticleTypes.ENCHANTED_HIT;
		}
		return value;
	}
}
