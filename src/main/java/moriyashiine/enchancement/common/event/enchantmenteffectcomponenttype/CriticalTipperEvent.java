/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.world.item.effects.CriticalTipperEffect;
import moriyashiine.strawberrylib.api.event.ModifyCriticalStatusEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CriticalTipperEvent implements ModifyCriticalStatusEvent {
	public static ParticleType<?> particleType = null;

	@Override
	public TriState isCritical(Player attacker, Entity target, float attackCooldownProgress) {
		if (forceCritical(attacker, target, attackCooldownProgress)) {
			particleType = CriticalTipperEffect.getParticleType(attacker.getMainHandItem());
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}

	private static boolean forceCritical(Player attacker, Entity target, float attackCooldownProgress) {
		if (EnchantmentHelper.has(attacker.getMainHandItem(), ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER) && attackCooldownProgress > 0.9F) {
			float distanceLeniency = CriticalTipperEffect.getDistanceLeniency(attacker.getMainHandItem(), attacker.getRandom());
			return attacker.distanceTo(target) > attacker.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) - distanceLeniency;
		}
		return false;
	}
}
