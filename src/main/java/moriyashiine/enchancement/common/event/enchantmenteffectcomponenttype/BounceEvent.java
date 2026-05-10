/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BounceComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BounceEvent implements PreventFallDamageEvent {
	@Override
	public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
		if (!source.is(DamageTypes.STALAGMITE) && fallDistance > entity.getMaxFallDistance() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.BOUNCE)) {
			if (fallDistance > entity.getMaxFallDistance() + 1) {
				SLibUtils.playSound(entity, SoundEvents.SLIME_BLOCK_FALL);
				BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(entity);
				double bounceStrength = EnchancementUtil.altLog(1.05, fallDistance / 7, 1 / 16F);
				if (shouldBounce(entity, bounceComponent)) {
					bounceComponent.bounce(bounceStrength);
				}
				ModEntityComponents.CHARGE_JUMP.maybeGet(entity).ifPresent(chargeJumpComponent -> chargeJumpComponent.addChargeDelayed(bounceStrength * 4));
			}
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}

	private static boolean shouldBounce(LivingEntity entity, BounceComponent bounceComponent) {
		boolean bounce = !SLibUtils.isCrouching(entity, true);
		if (bounceComponent.hasInvertedBounce()) {
			bounce = !bounce;
		}
		return bounce;
	}
}
