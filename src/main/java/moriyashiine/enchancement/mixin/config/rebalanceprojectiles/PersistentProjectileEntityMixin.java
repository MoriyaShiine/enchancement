/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$rebalanceProjectiles(PersistentProjectileEntity instance, Operation<Vec3d> original, @Local Entity entity) {
		Vec3d velocity = original.call(instance);
		if (ModConfig.rebalanceProjectiles && instance.getOwner() instanceof PlayerEntity) {
			velocity = velocity.multiply(1 / 1.25);
			if (entity instanceof LivingEntity living) {
				velocity = velocity.multiply(MathHelper.lerp(Math.min(20, living.getArmor()) / 20F, 1, 1.5F));
			}
		}
		return velocity;
	}
}
