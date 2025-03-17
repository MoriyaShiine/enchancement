/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffect;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.enchantment.effect.ModifySubmergedMovementSpeedEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ModifySubmergedMovementSpeedEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * (1 + ModifySubmergedMovementSpeedEffect.getValue(living)));
	}
}
