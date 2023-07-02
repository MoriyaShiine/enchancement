/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;

public class LeechEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (source.getSource() instanceof TridentEntity trident && !entity.blockedByShield(source)) {
			ModEntityComponents.LEECH.maybeGet(trident).ifPresent(leechComponent -> {
				if (leechComponent.hasLeech() && leechComponent.getStuckEntity() == null) {
					if (trident.getOwner() != entity) {
						entity.getWorld().getEntitiesByClass(TridentEntity.class, entity.getBoundingBox().expand(1), foundTrident -> true).forEach(otherTrident -> ModEntityComponents.LEECH.maybeGet(otherTrident).ifPresent(otherLeech -> {
							if (otherLeech.getStuckEntity() == entity) {
								otherLeech.setStuckEntityId(-2);
								otherLeech.sync();
							}
						}));
						leechComponent.setStuckEntityId(entity.getId());
						leechComponent.sync();
					}
				}
			});
		}
		return true;
	}
}
