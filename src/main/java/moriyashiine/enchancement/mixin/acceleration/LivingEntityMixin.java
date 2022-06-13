/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.acceleration;

import moriyashiine.enchancement.common.component.entity.AccelerationComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$acceleration(float value) {
		AccelerationComponent accelerationComponent = ModEntityComponents.ACCELERATION.getNullable(this);
		if (accelerationComponent != null && accelerationComponent.hasAcceleration()) {
			value = Math.max(0, value - (accelerationComponent.getSpeedMultiplier() - 1) * 2);
		}
		return value;
	}
}
