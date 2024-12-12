/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getFishingTimeReduction", at = @At("RETURN"))
	private static float enchancement$rebalanceEnchantments(float original, ServerWorld world, ItemStack stack, Entity user) {
		if (ModConfig.rebalanceEnchantments) {
			return original + EnchantmentHelper.getFishingLuckBonus(world, stack, user) * 5;
		}
		return original;
	}
}
