/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Potions.class)
public class PotionsMixin {
	@ModifyVariable(method = "register", at = @At("HEAD"), argsOnly = true)
	private static Potion enchancement$rebalanceStatusEffects(Potion potion) {
		if (ModConfig.rebalanceStatusEffects && potion.name().contains("turtle_master")) {
			List<MobEffectInstance> effects = new ArrayList<>();
			for (MobEffectInstance effect : potion.getEffects()) {
				if (effect.getEffect() == MobEffects.RESISTANCE) {
					effects.add(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() - 1));
				} else {
					effects.add(effect);
				}
			}
			return new Potion(potion.name(), effects.toArray(new MobEffectInstance[0]));
		}
		return potion;
	}
}
