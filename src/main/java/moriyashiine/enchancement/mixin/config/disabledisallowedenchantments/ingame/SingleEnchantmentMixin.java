/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.providers.SingleEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SingleEnchantment.class)
public class SingleEnchantmentMixin {
	@Inject(method = "enchant", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
		EnchancementUtil.cachedApplyStack = item;
	}

	@Inject(method = "enchant", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsTail(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
		EnchancementUtil.cachedApplyStack = null;
	}
}
