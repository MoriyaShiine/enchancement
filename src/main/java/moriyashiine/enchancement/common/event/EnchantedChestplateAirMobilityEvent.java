/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.AirMobilityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class EnchantedChestplateAirMobilityEvent implements MultiplyMovementSpeedEvent {
	@Override
	public float multiply(float currentMultiplier, World world, LivingEntity living) {
		if (ModConfig.enchantedChestplatesIncreaseAirMobility && !living.isOnGround()) {
			AirMobilityComponent airMobilityComponent = ModEntityComponents.AIR_MOBILITY.getNullable(living);
			if (airMobilityComponent != null && airMobilityComponent.getTicksInAir() > 10) {
				return currentMultiplier * 1.5F;
			}
		}
		return currentMultiplier;
	}

	@Override
	public int getPriority() {
		return 1001;
	}
}
