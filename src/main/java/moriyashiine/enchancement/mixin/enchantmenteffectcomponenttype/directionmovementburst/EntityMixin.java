/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.directionmovementburst;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.DirectionMovementBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "getFinalGravity", at = @At("RETURN"))
	private double enchancement$directionMovementBurst(double original) {
		DirectionMovementBurstComponent directionMovementBurstComponent = ModEntityComponents.DIRECTION_MOVEMENT_BURST.getNullable(this);
		if (directionMovementBurstComponent != null && directionMovementBurstComponent.preventFalling()) {
			return 0;
		}
		return original;
	}
}
