/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import moriyashiine.strawberrylib.api.event.ModifyDamageTakenEvent;
import moriyashiine.strawberrylib.api.event.ModifyStackDamageEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RageEvent {
	public static class DamageDealtBonus implements ModifyStackDamageEvent {
		@Override
		public float modify(ServerLevel level, ItemStack stack, Entity victim, DamageSource source, float damage) {
			if (source.getDirectEntity() instanceof LivingEntity living) {
				return RageEffect.getDamageDealtModifier(living, stack);
			}
			return 0;
		}
	}

	public static class DamageTakenReduction implements ModifyDamageTakenEvent {
		@Override
		public float modify(Phase phase, LivingEntity victim, ServerLevel level, DamageSource source) {
			if (phase == Phase.FINAL && !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				return RageEffect.getDamageTakenModifier(victim);
			}
			return 1;
		}
	}

	public static class SpeedBonus implements MultiplyMovementSpeedEvent {
		@Override
		public float multiply(float currentMultiplier, Level level, LivingEntity living) {
			return MultiplyMovementSpeedEvent.capMovementMultiplier(currentMultiplier * RageEffect.getMovementSpeedModifier(living));
		}
	}
}
