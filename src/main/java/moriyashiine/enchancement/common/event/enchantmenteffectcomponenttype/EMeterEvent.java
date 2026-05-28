/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EMeterEvent implements CappedMultiplyDeltaMovementEvent {
	public static void init() {
		CappedMultiplyDeltaMovementEvent.EVENT.register(new EMeterEvent());
	}

	@Override
	public float multiply(Level level, LivingEntity living) {
		return 1 + ModEntityComponents.E_METER.get(living).getSpeedBonus();
	}
}
