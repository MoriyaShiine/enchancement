/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class RageEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * RageEffect.getMovementSpeedModifier(living));
	}
}
