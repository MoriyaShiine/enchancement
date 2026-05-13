/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EMeterEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, Level level, LivingEntity living) {
		return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * (1 + ModEntityComponents.E_METER.get(living).getSpeedBonus()));
	}
}
