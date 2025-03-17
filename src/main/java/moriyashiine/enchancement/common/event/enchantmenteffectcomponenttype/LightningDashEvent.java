/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

public class LightningDashEvent implements PreventFallDamageEvent {
	@Override
	public boolean shouldNotTakeFallDamage(World world, LivingEntity entity, float fallDistance, float damageMultiplier, DamageSource damageSource) {
		LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.getNullable(entity);
		return lightningDashComponent != null && lightningDashComponent.isSmashing();
	}
}
