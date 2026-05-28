/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class InCombatEvent implements ServerLivingEntityEvents.AfterDamage {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new InCombatEvent());
	}

	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (source.getEntity() instanceof LivingEntity) {
			ModEntityComponents.IN_COMBAT.get(entity).setInCombat();
		}
	}
}
