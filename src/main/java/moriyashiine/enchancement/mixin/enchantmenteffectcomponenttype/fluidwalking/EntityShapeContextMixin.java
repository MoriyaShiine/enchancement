/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityShapeContext.class)
public class EntityShapeContextMixin {
	@ModifyExpressionValue(method = "canWalkOnFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canWalkOnFluid(Lnet/minecraft/fluid/FluidState;)Z"))
	private boolean enchancement$fluidWalking(boolean original, @Local LivingEntity living) {
		return original || EnchancementUtil.shouldFluidWalk(living);
	}
}
