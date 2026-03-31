/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCollisionContext.class)
public class EntityCollisionContextMixin {
	@ModifyExpressionValue(method = "canStandOnFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z"))
	private boolean enchancement$fluidWalking(boolean original, @Local(name = "livingEntity") LivingEntity livingEntity) {
		return original || EnchancementUtil.shouldFluidWalk(livingEntity);
	}
}
