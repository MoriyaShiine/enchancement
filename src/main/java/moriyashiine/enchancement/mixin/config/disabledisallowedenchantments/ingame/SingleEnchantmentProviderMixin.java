/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.provider.SingleEnchantmentProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SingleEnchantmentProvider.class)
public class SingleEnchantmentProviderMixin {
	@Inject(method = "provideEnchantments", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(ItemStack stack, ItemEnchantmentsComponent.Builder componentBuilder, Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
		EnchancementUtil.cachedApplyStack = stack;
	}

	@Inject(method = "provideEnchantments", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsTail(ItemStack stack, ItemEnchantmentsComponent.Builder componentBuilder, Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
		EnchancementUtil.cachedApplyStack = null;
	}
}
