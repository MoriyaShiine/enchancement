/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
import moriyashiine.enchancement.common.enchantment.effect.RotationBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyJumpVelocityEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class RotationBurstEvent implements ModifyJumpVelocityEvent {
	@Override
	public Vec3d modify(Vec3d velocity, LivingEntity entity) {
		RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.getNullable(entity);
		if (rotationBurstComponent != null && rotationBurstComponent.shouldWavedash()) {
			rotationBurstComponent.setCooldown(0);
			return velocity.multiply(RotationBurstEffect.getWavedashStrength(entity));
		}
		return velocity;
	}
}
