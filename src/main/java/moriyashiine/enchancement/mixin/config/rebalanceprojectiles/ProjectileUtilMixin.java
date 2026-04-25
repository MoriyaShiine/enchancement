/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
	@WrapOperation(method = "getEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"))
	private static AABB enchancement$rebalanceProjectiles(AABB instance, double amountToAddInAllDirections, Operation<AABB> original, @Local(name = "entity") Entity entity) {
		if (ModConfig.rebalanceProjectiles) {
			Vec3 delta = EnchancementUtil.getSyncedDeltaMovement(entity);
			Vec3 adjustedDelta = new Vec3(delta.x(), Math.max(0, delta.y()), delta.z());
			amountToAddInAllDirections *= Mth.lerp(Math.min(1, adjustedDelta.length() * 2), 1, 5);
		}
		return original.call(instance, amountToAddInAllDirections);
	}
}
