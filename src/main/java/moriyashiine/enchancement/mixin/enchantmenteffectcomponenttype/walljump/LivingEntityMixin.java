/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.walljump;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyExpressionValue(method = "travelRidden", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canSimulateMovement()Z"))
	private boolean enchancement$wallJump(boolean original) {
		return original || ModEntityComponents.WALL_JUMP.get(this).hasJumped();
	}
}
