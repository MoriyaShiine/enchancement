/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LightningDashEvent implements PreventFallDamageEvent {
	@Override
	public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
		LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.getNullable(entity);
		if (lightningDashComponent != null && lightningDashComponent.isSmashing()) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
}
