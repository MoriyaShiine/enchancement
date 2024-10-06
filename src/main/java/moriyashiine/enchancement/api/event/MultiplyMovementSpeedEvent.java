/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public interface MultiplyMovementSpeedEvent {
	int MAXIMUM_MOVEMENT_MULTIPLIER = 2;

	Event<MultiplyMovementSpeedEvent> EVENT = EventFactory.createArrayBacked(MultiplyMovementSpeedEvent.class, listeners -> (currentMultiplier, world, living) -> {
		List<MultiplyMovementSpeedEvent> events = new ArrayList<>(Arrays.asList(listeners));
		events.sort(Comparator.comparingInt(MultiplyMovementSpeedEvent::getPriority));
		for (MultiplyMovementSpeedEvent event : events) {
			currentMultiplier = event.multiply(currentMultiplier, world, living);
		}
		return currentMultiplier;
	});

	float multiply(float currentMultiplier, World world, LivingEntity living);

	default int getPriority() {
		return 1000;
	}

	static float capMovementMultiplier(float multiplier) {
		return Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, multiplier);
	}
}
