/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Shulker.ShulkerPeekGoal.class, Shulker.ShulkerAttackGoal.class})
public class ShulkerEntityGoalMixin {
	@Unique
	private Shulker cachedEntity = null;

	@WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Shulker;getTarget()Lnet/minecraft/world/entity/LivingEntity;"))
	private LivingEntity enchancement$buryEntity(Shulker instance, Operation<LivingEntity> original) {
		cachedEntity = instance;
		return original.call(instance);
	}

	@ModifyReturnValue(method = "canUse", at = @At("RETURN"))
	private boolean enchancement$buryEntity(boolean original) {
		if (original && cachedEntity != null && ModEntityComponents.BURY_ENTITY.get(cachedEntity).getBuryPos() != null) {
			cachedEntity = null;
			return false;
		}
		return original;
	}
}
