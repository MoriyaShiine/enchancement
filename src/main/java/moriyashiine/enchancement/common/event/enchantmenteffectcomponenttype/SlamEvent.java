/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlamEvent {
	public static class FallImmunity implements PreventFallDamageEvent {
		@Override
		public TriState preventsFallDamage(World world, LivingEntity entity, double fallDistance, float damagePerDistance, DamageSource damageSource) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
			if (slamComponent != null && slamComponent.isSlamming()) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}

	public static class JumpBoost implements ModifyMovementEvents.JumpVelocity {
		@Override
		public Vec3d modify(Vec3d velocity, LivingEntity entity) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
			if (slamComponent != null) {
				return velocity.multiply(1, slamComponent.getJumpBoostStrength(), 1);
			}
			return velocity;
		}
	}
}
