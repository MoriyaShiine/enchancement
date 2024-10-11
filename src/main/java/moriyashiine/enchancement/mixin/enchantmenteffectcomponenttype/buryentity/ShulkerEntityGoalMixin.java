/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ShulkerEntity.PeekGoal.class, ShulkerEntity.ShootBulletGoal.class})
public class ShulkerEntityGoalMixin {
	@Unique
	private ShulkerEntity cachedEntity = null;

	@WrapOperation(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ShulkerEntity;getTarget()Lnet/minecraft/entity/LivingEntity;"))
	private LivingEntity enchancement$buryEntity(ShulkerEntity instance, Operation<LivingEntity> original) {
		cachedEntity = instance;
		return original.call(instance);
	}

	@ModifyReturnValue(method = "canStart", at = @At("RETURN"))
	private boolean enchancement$buryEntity(boolean original) {
		if (original && cachedEntity != null && ModEntityComponents.BURY_ENTITY.get(cachedEntity).getBuryPos() != null) {
			cachedEntity = null;
			return false;
		}
		return original;
	}
}
