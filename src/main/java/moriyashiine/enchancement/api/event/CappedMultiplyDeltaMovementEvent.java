/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.api.event;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.level.Level;

public interface CappedMultiplyDeltaMovementEvent {
	int MAXIMUM_MOVEMENT_MULTIPLIER = 2;

	Event<CappedMultiplyDeltaMovementEvent> EVENT = EventFactory.createArrayBacked(CappedMultiplyDeltaMovementEvent.class, events -> (level, living) -> {
		float multiplier = 1;
		for (CappedMultiplyDeltaMovementEvent event : events) {
			multiplier *= event.multiply(level, living);
		}
		return multiplier;
	});

	float multiply(Level level, LivingEntity living);

	static float getJumpStrength(LivingEntity entity, float multiplier) {
		double jumpRatio = entity.getJumpPower() / entity.getAttributeBaseValue(Attributes.JUMP_STRENGTH);
		double speedRatio = entity.getAttributeValue(Attributes.MOVEMENT_SPEED) / entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
		return (float) (Mth.clamp(jumpRatio * Math.min(1, speedRatio), 2 / 3F, 1.2F) * entity.getAttributeValue(Attributes.JUMP_STRENGTH) * multiplier);
	}

	static float getMovementMultiplier(LivingEntity entity, float eventMultiplier) {
		float rawMultiplier = EVENT.invoker().multiply(entity.level(), entity);
		if (eventMultiplier != 1 && entity.isUsingItem() && !entity.isPassenger()) {
			rawMultiplier *= EnchancementUtil.getItemUseSpeedMultiplier(entity.getUseItem(), entity.getUseItem().getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT).speedMultiplier());
		}
		float multiplier = 1 + (Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, rawMultiplier) - 1) * eventMultiplier;
		double speedRatio = entity.getAttributeValue(Attributes.MOVEMENT_SPEED) / entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
		return multiplier * (float) (Mth.clamp(speedRatio, 2 / 3F, 1.2F));
	}
}
