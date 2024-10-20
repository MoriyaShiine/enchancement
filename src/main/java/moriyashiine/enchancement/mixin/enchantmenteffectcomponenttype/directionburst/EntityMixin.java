/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.directionburst;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "getFinalGravity", at = @At("RETURN"))
	private double enchancement$directionBurst(double original) {
		DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.getNullable(this);
		if (directionBurstComponent != null && directionBurstComponent.preventFalling()) {
			return 0;
		}
		return original;
	}
}
