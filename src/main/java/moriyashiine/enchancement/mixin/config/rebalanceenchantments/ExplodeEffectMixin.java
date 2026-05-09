/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.item.enchantment.effects.ExplodeEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Mixin(ExplodeEffect.class)
public class ExplodeEffectMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/SimpleExplosionDamageCalculator;<init>(ZZLjava/util/Optional;Ljava/util/Optional;)V"), index = 2)
	private Optional<Float> enchancement$rebalanceEnchantments(Optional<Float> knockbackMultiplier) {
		if (ModConfig.rebalanceEnchantments) {
			return knockbackMultiplier.map(knockback -> knockback * 2 / 3F);
		}
		return knockbackMultiplier;
	}
}
