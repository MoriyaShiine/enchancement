package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;

public class LeechingTridentEvent implements ServerLivingEntityEvents.AfterDamage {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new LeechingTridentEvent());
	}

	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (!blocked && source.getDirectEntity() instanceof ThrownTrident trident) {
			LeechingTridentComponent leechingTrident = EnchancementEntityComponents.LEECHING_TRIDENT.get(trident);
			if (leechingTrident.hasLeech() && leechingTrident.getStuckEntity() == null) {
				if (trident.getOwner() != entity) {
					entity.level().getEntitiesOfClass(ThrownTrident.class, entity.getBoundingBox().inflate(1), _ -> true).forEach(otherTrident -> {
						LeechingTridentComponent otherLeechingTrident = EnchancementEntityComponents.LEECHING_TRIDENT.get(otherTrident);
						if (otherLeechingTrident.getStuckEntity() == entity) {
							otherLeechingTrident.unleech();
						}
					});
					leechingTrident.setStuckEntity(entity);
					leechingTrident.sync();
				}
			}
		}
	}
}
