/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Potions.class)
public class PotionsMixin {
	@ModifyVariable(method = "register", at = @At("HEAD"), argsOnly = true)
	private static Potion enchancement$rebalanceStatusEffects(Potion value) {
		if (ModConfig.rebalanceStatusEffects && value.getBaseName().contains("turtle_master")) {
			List<StatusEffectInstance> effects = new ArrayList<>();
			for (StatusEffectInstance effect : value.getEffects()) {
				if (effect.getEffectType() == StatusEffects.RESISTANCE) {
					effects.add(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier() - 1));
				} else {
					effects.add(effect);
				}
			}
			return new Potion(value.getBaseName(), effects.toArray(new StatusEffectInstance[0]));
		}
		return value;
	}
}
