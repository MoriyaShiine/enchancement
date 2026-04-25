/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.world.item.effects.RotationBurstEffect;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RotationBurstEvent implements ModifyMovementEvents.JumpDelta {
	@Override
	public Vec3 modify(Vec3 delta, LivingEntity entity) {
		RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.getNullable(entity);
		if (rotationBurstComponent != null && rotationBurstComponent.shouldWavedash()) {
			rotationBurstComponent.setCooldown(0);
			return delta.scale(RotationBurstEffect.getWavedashStrength(entity));
		}
		return delta;
	}
}
