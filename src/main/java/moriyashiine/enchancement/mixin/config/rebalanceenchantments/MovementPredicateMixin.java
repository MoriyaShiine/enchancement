/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.predicate.entity.MovementPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MovementPredicate.class)
public class MovementPredicateMixin {
	@ModifyVariable(method = "test", at = @At("HEAD"), ordinal = 3, argsOnly = true)
	private double enchancement$rebalanceEnchantments(double value) {
		if (ModConfig.rebalanceEnchantments) {
			return value * 1.5F;
		}
		return value;
	}
}
