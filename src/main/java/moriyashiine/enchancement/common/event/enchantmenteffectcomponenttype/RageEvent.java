/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import moriyashiine.strawberrylib.api.event.ModifyDamageTakenEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class RageEvent {
	public static class DamageDealtBonus implements ModifyDamageTakenEvent {
		@Override
		public float modify(Phase phase, float amount, ServerWorld world, DamageSource source, LivingEntity victim) {
			if (phase == Phase.BASE && source.getSource() instanceof LivingEntity living) {
				return RageEffect.getDamageDealtModifier(living, living.getMainHandStack());
			}
			return 0;
		}
	}

	public static class DamageTakenReduction implements ModifyDamageTakenEvent {
		@Override
		public float modify(Phase phase, float amount, ServerWorld world, DamageSource source, LivingEntity victim) {
			if (phase == Phase.FINAL && !source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				return RageEffect.getDamageTakenModifier(victim);
			}
			return 1;
		}
	}

	public static class SpeedBonus implements MultiplyMovementSpeedEvent {
		@Override
		public float multiply(float currentMultiplier, World world, LivingEntity living) {
			return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * RageEffect.getMovementSpeedModifier(living));
		}
	}
}
