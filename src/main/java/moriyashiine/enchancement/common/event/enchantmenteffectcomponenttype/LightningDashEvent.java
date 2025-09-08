/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

public class LightningDashEvent implements PreventFallDamageEvent {
	@Override
	public TriState preventsFallDamage(World world, LivingEntity entity, double fallDistance, float damagePerDistance, DamageSource damageSource) {
		LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.getNullable(entity);
		if (lightningDashComponent != null && lightningDashComponent.isSmashing()) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
}
