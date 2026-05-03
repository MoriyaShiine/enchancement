/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getFishingTimeReduction", at = @At("RETURN"))
	private static float enchancement$rebalanceEnchantments(float original, ServerLevel serverLevel, ItemStack rod, Entity fisher) {
		if (ModConfig.rebalanceEnchantments) {
			return original + EnchantmentHelper.getFishingLuckBonus(serverLevel, rod, fisher) * 5;
		}
		return original;
	}

	@ModifyReturnValue(method = "getTridentSpinAttackStrength", at = @At("RETURN"))
	private static float enchancement$rebalanceEnchantments(float original) {
		if (ModConfig.rebalanceEnchantments) {
			return original * 0.7F;
		}
		return original;
	}
}
