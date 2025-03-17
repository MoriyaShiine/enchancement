/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

public class FluidWalkingEvent implements PreventFallDamageEvent {
	@Override
	public boolean shouldNotTakeFallDamage(World world, LivingEntity entity, float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return fallDistance > entity.getSafeFallDistance() && !world.getFluidState(entity.getSteppingPos()).isEmpty() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
