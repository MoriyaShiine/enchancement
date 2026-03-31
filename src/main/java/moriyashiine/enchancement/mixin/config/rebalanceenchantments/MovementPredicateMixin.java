/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.advancements.criterion.MovementPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MovementPredicate.class)
public class MovementPredicateMixin {
	@ModifyVariable(method = "matches", at = @At("HEAD"), argsOnly = true, ordinal = 3)
	private double enchancement$rebalanceEnchantments(double fallDistance) {
		if (ModConfig.rebalanceEnchantments) {
			return fallDistance * 1.5F;
		}
		return fallDistance;
	}
}
