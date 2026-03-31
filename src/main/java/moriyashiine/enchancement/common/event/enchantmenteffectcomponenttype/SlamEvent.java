/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SlamEvent {
	public static class FallImmunity implements PreventFallDamageEvent {
		@Override
		public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
			if (slamComponent != null && slamComponent.isSlamming()) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}

	public static class JumpBoost implements ModifyMovementEvents.JumpDelta {
		@Override
		public Vec3 modify(Vec3 delta, LivingEntity entity) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
			if (slamComponent != null) {
				return delta.multiply(1, slamComponent.getJumpBoostStrength(), 1);
			}
			return delta;
		}
	}
}
