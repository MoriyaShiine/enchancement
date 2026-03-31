/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FluidWalkingEvent implements PreventFallDamageEvent {
	@Override
	public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
		if (fallDistance > entity.getMaxFallDistance() && !level.getFluidState(entity.getOnPos()).isEmpty() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING)) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
}
