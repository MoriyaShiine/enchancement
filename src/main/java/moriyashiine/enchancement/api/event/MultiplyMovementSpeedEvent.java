/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.api.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.world.item.effects.ModifySubmergedMovementSpeedEffect;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// todo probably clean this up a lot
public interface MultiplyMovementSpeedEvent {
	int MAXIMUM_MOVEMENT_MULTIPLIER = 2;

	Event<MultiplyMovementSpeedEvent> EVENT = EventFactory.createArrayBacked(MultiplyMovementSpeedEvent.class, listeners -> (currentMultiplier, level, living) -> {
		List<MultiplyMovementSpeedEvent> events = new ArrayList<>(Arrays.asList(listeners));
		events.sort(Comparator.comparingInt(MultiplyMovementSpeedEvent::getPriority));
		for (MultiplyMovementSpeedEvent event : events) {
			currentMultiplier = event.multiply(currentMultiplier, level, living);
		}
		return currentMultiplier;
	});

	float multiply(float currentMultiplier, Level level, LivingEntity living);

	default int getPriority() {
		return 1000;
	}

	static float capMovementMultiplier(float multiplier) {
		return Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, multiplier);
	}

	static float getJumpStrength(LivingEntity entity, float multiplier) {
		double jumpRatio = entity.getJumpPower() / entity.getAttributeBaseValue(Attributes.JUMP_STRENGTH);
		double speedRatio = entity.getAttributeValue(Attributes.MOVEMENT_SPEED) / entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
		if (entity.isSprinting()) {
			speedRatio /= 1.3;
		}
		return (float) (Mth.clamp(jumpRatio * Math.min(1, speedRatio), 2 / 3F, 1.2F) * entity.getAttributeValue(Attributes.JUMP_STRENGTH) * multiplier);
	}

	static float getMovementMultiplier(LivingEntity entity) {
		double multiplier = 1 + ModifySubmergedMovementSpeedEffect.getValue(entity);
		multiplier *= RageEffect.getMovementSpeedModifier(entity);
		if (entity.isUsingItem() && !entity.isPassenger()) {
			multiplier *= getItemUseSpeedMultiplier(entity.getUseItem(), entity.getUseItem().getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT).speedMultiplier());
		}
		if (entity.isSprinting()) {
			multiplier /= 1.3;
		}
		multiplier = Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, multiplier);
		return (float) Mth.clamp(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * multiplier / entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), 2 / 3F, 1.2F);
	}

	static float getItemUseSpeedMultiplier(ItemStack stack, float original) {
		if (ModConfig.rebalanceEquipment) {
			Item item = stack.getItem();
			if (item instanceof BowItem || item instanceof MaceItem || item instanceof TridentItem) {
				return Math.min(1, original * 3);
			}
		}
		return original;
	}
}
