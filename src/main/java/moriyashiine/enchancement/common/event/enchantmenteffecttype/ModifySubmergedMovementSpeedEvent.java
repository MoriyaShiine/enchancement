/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffecttype;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.common.world.item.effects.ModifySubmergedMovementSpeedEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ModifySubmergedMovementSpeedEvent implements CappedMultiplyDeltaMovementEvent {
	@Override
	public float multiply(Level level, LivingEntity living) {
		return 1 + ModifySubmergedMovementSpeedEffect.getValue(living);
	}
}
