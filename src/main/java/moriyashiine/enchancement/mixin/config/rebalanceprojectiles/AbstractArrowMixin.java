/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
	@WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	private Vec3 enchancement$rebalanceProjectiles(AbstractArrow instance, Operation<Vec3> original, @Local(name = "entity") Entity entity) {
		Vec3 delta = original.call(instance);
		if (ModConfig.rebalanceProjectiles && instance.getOwner() instanceof Player) {
			delta = delta.scale(1 / 1.25);
			if (entity instanceof LivingEntity living) {
				delta = delta.scale(Mth.lerp(Math.min(20, living.getArmorValue()) / 20F, 1, 1.5F));
			}
		}
		return delta;
	}
}
