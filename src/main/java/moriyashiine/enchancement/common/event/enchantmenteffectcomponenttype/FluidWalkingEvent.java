/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FluidWalkingEvent {
	public static class DolphinsGrace implements ModifyMovementEvents.MovementDelta {
		@Override
		public Vec3 modify(Vec3 delta, LivingEntity entity) {
			if (entity.hasEffect(MobEffects.DOLPHINS_GRACE) && shouldApply(entity)) {
				return delta.scale(1.75);
			}
			return delta;
		}

		public static boolean shouldApply(LivingEntity entity) {
			return entity.isInWater() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
		}
	}

	public static class FallImmunity implements PreventFallDamageEvent {
		@Override
		public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
			if (fallDistance > entity.getMaxFallDistance() && !level.getFluidState(entity.getOnPos()).isEmpty() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}
}
