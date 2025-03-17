/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.event.internal.SyncVelocitiesEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
	@WrapOperation(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(D)Lnet/minecraft/util/math/Box;"))
	private static Box enchancement$rebalanceProjectiles(Box instance, double value, Operation<Box> original, @Local(ordinal = 2) Entity entity) {
		if (ModConfig.rebalanceProjectiles) {
			Vec3d velocity = SyncVelocitiesEvent.VELOCITIES.getOrDefault(entity.getUuid(), entity.getVelocity());
			Vec3d adjustedVelocity = new Vec3d(velocity.getX(), Math.max(0, velocity.getY()), velocity.getZ());
			value *= MathHelper.lerp(Math.min(1, adjustedVelocity.length() * 2), 1, 5);
		}
		return original.call(instance, value);
	}
}
