/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;

public class LeechingTridentEvent implements ServerLivingEntityEvents.AfterDamage {
	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (!blocked && source.getDirectEntity() instanceof ThrownTrident trident) {
			LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(trident);
			if (leechingTridentComponent.hasLeech() && leechingTridentComponent.getStuckEntity() == null) {
				if (trident.getOwner() != entity) {
					entity.level().getEntitiesOfClass(ThrownTrident.class, entity.getBoundingBox().inflate(1), _ -> true).forEach(otherTrident -> {
						LeechingTridentComponent otherLeechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(otherTrident);
						if (otherLeechingTridentComponent.getStuckEntity() == entity) {
							otherLeechingTridentComponent.unleech();
						}
					});
					leechingTridentComponent.setStuckEntity(entity);
					leechingTridentComponent.sync();
				}
			}
		}
	}
}
