/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.airjump;

import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$airJump(float value) {
		AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.getNullable(this);
		if (airJumpComponent != null && airJumpComponent.hasAirJump()) {
			return Math.max(0, value - (airJumpComponent.getMaxJumps() - airJumpComponent.getJumpsLeft()));
		}
		return value;
	}
}
