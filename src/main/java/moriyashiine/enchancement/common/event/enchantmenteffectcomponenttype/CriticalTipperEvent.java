/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.enchantment.effect.CriticalTipperEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.strawberrylib.api.event.ModifyCriticalStatusEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleType;

public class CriticalTipperEvent implements ModifyCriticalStatusEvent {
	public static ParticleType<?> particleType = null;

	@Override
	public TriState isCritical(PlayerEntity attacker, Entity target, float attackCooldownProgress) {
		if (forceCritical(attacker, target, attackCooldownProgress)) {
			particleType = CriticalTipperEffect.getParticleType(attacker.getMainHandStack());
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}

	private static boolean forceCritical(PlayerEntity attacker, Entity target, float attackCooldownProgress) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(attacker.getMainHandStack(), ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER) && attackCooldownProgress > 0.9F) {
			float distanceLeniency = CriticalTipperEffect.getDistanceLeniency(attacker.getMainHandStack(), attacker.getRandom());
			return attacker.distanceTo(target) > attacker.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE) - distanceLeniency;
		}
		return false;
	}
}
