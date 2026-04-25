/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffecttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.world.item.effects.ModifySubmergedMovementSpeedEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ModifySubmergedMovementSpeedEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, Level level, LivingEntity living) {
		return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * (1 + ModifySubmergedMovementSpeedEffect.getValue(living)));
	}
}
