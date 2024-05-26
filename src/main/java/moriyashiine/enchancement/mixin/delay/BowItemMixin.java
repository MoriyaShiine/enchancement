/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.delay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RangedWeaponItem.class, priority = 1001)
public class BowItemMixin {
	@WrapOperation(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void enchancement$delay(RangedWeaponItem instance, LivingEntity shooter, ProjectileEntity projectileEntity, int index, float speed, float divergence, float yaw, LivingEntity target, Operation<Void> original) {
		original.call(instance, shooter, projectileEntity, index, speed, divergence, yaw, target);
		ModEntityComponents.DELAY.maybeGet(projectileEntity).ifPresent(delayComponent -> {
			if (delayComponent.hasDelay()) {
				delayComponent.setCachedSpeed(speed);
				delayComponent.setCachedDivergence(divergence);
			}
		});
	}
}
