/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.airjump;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getUnsafeFallDistance", at = @At("RETURN"))
	private double enchancement$airJump(double original) {
		AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.getNullable(this);
		if (airJumpComponent != null && airJumpComponent.hasAirJump()) {
			original -= airJumpComponent.getMaxJumps() - airJumpComponent.getJumpsLeft();
		}
		return original;
	}
}
