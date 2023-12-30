/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.projectilesnegatevelocity;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tiltScreen(DD)V"))
	private void enchancement$projectilesNegateVelocity(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.projectilesNegateVelocity && source.getSource() instanceof ProjectileEntity) {
			setVelocity(0, Math.min(0, getVelocity().getY()), 0);
			velocityModified = true;
		}
	}
}
