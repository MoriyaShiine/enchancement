/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.api.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.enchantment.effect.ModifySubmergedMovementSpeedEffect;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.MathHelper;
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

	static float getJumpStrength(LivingEntity entity, float multiplier) {
		double jumpRatio = entity.getJumpVelocity() / entity.getAttributeBaseValue(EntityAttributes.JUMP_STRENGTH);
		double speedRatio = entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED) / entity.getAttributeBaseValue(EntityAttributes.MOVEMENT_SPEED);
		if (entity.isSprinting()) {
			speedRatio /= 1.3;
		}
		return (float) (MathHelper.clamp(jumpRatio * Math.min(1, speedRatio), 2 / 3F, 1.2F) * entity.getAttributeValue(EntityAttributes.JUMP_STRENGTH) * multiplier);
	}

	static float getMovementMultiplier(LivingEntity entity) {
		double multiplier = 1 + ModifySubmergedMovementSpeedEffect.getValue(entity);
		multiplier *= RageEffect.getMovementSpeedModifier(entity);
		if (entity.isUsingItem()) {
			if (ModConfig.rebalanceEquipment && entity.getActiveItem().getItem() instanceof BowItem) {
				multiplier *= 0.6F;
			} else {
				multiplier *= 0.2;
			}
		}
		if (entity.isSprinting()) {
			multiplier /= 1.3;
		}
		multiplier = Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, multiplier);
		return (float) MathHelper.clamp(entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED) * multiplier / entity.getAttributeBaseValue(EntityAttributes.MOVEMENT_SPEED), 2 / 3F, 1.2F);
	}
}
