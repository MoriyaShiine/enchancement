/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class InCombatEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (source.getAttacker() instanceof LivingEntity) {
			ModEntityComponents.IN_COMBAT.get(entity).setInCombat();
		}
		return true;
	}
}
