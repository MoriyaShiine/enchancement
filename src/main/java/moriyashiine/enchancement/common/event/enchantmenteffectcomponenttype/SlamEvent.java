/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyJumpVelocityEvent;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlamEvent {
	public static class FallImmunity implements PreventFallDamageEvent {
		@Override
		public boolean shouldNotTakeFallDamage(World world, LivingEntity entity, float fallDistance, float damageMultiplier, DamageSource damageSource) {
			SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
			return slamComponent != null && slamComponent.isSlamming();
		}
	}

	public static class JumpBoost implements ModifyJumpVelocityEvent {
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
