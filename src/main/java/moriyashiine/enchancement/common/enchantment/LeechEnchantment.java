/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;

public class LeechEnchantment extends NoRiptideEnchantment {
	public LeechEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (target.getWorld().isClient) {
			for (int i = 0; i < 6; i++) {
				target.getWorld().addParticle(ParticleTypes.DAMAGE_INDICATOR, target.getParticleX(1), target.getBodyY(0.5), target.getParticleZ(1), 0, 0, 0);
			}
		} else {
			user.heal(0.5F);
		}
	}
}
