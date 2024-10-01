/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class AllowInterruptionEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(entity.getActiveItem(), ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION)) {
			entity.stopUsingItem();
		}
		return true;
	}
}
