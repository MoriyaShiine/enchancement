/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
	public static void init() {
		ModifyStackDamageEvent.ADD.register(new DamageDealtBonus());
		ModifyDamageTakenEvent.MULTIPLY_TOTAL.register(new DamageTakenReduction());
		CappedMultiplyDeltaMovementEvent.EVENT.register(new SpeedBonus());
	}

	private static class DamageDealtBonus implements ModifyStackDamageEvent {
		@Override
		public float modify(ServerLevel level, ItemStack stack, Entity victim, DamageSource source, float damage) {
			if (EnchancementUtil.shouldApplyWeaponCooldown() && source.getDirectEntity() instanceof LivingEntity living) {
				return RageEffect.getDamageDealtModifier(living, stack);
			}
			return 0;
		}
	}

	private static class DamageTakenReduction implements ModifyDamageTakenEvent {
		@Override
		public float modify(Phase phase, LivingEntity victim, ServerLevel level, DamageSource source) {
			if (phase == Phase.FINAL && !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
				return RageEffect.getDamageTakenModifier(victim);
			}
			return 1;
		}
	}

	private static class SpeedBonus implements CappedMultiplyDeltaMovementEvent {
		@Override
		public float multiply(Level level, LivingEntity living) {
			return RageEffect.getMovementSpeedModifier(living);
		}
	}
}
