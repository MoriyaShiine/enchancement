/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;

public class LeechingTridentEvent implements ServerLivingEntityEvents.AfterDamage {
	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (source.getSource() instanceof TridentEntity trident && !entity.blockedByShield(source)) {
			LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(trident);
			if (leechingTridentComponent.hasLeech() && leechingTridentComponent.getStuckEntity() == null) {
				if (trident.getOwner() != entity) {
					entity.getWorld().getEntitiesByClass(TridentEntity.class, entity.getBoundingBox().expand(1), foundTrident -> true).forEach(otherTrident -> {
						LeechingTridentComponent otherLeechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(otherTrident);
						if (otherLeechingTridentComponent.getStuckEntity() == entity) {
							otherLeechingTridentComponent.setStuckEntityId(-2);
							otherLeechingTridentComponent.sync();
						}
					});
					leechingTridentComponent.setStuckEntityId(entity.getId());
					leechingTridentComponent.sync();
				}
			}
		}
	}
}
