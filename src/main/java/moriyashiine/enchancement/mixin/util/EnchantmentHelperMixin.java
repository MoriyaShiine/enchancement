/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void enchancement$fixExclusiveEnchantmentsAndForceAtLeastOneEnchantment(FeatureSet enabledFeatures, int level, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		if (!stack.isOf(Items.BOOK)) {
			List<EnchantmentLevelEntry> entries = cir.getReturnValue();
			for (int i = entries.size() - 1; i >= 0; i--) {
				if (!stack.canBeEnchantedWith(entries.get(i).enchantment, EnchantingContext.RANDOM_ENCHANTMENT)) {
					entries.remove(i);
				}
			}
			if (entries.isEmpty()) {
				for (int i = 0; i < Registries.ENCHANTMENT.size(); i++) {
					Enchantment enchantment = Registries.ENCHANTMENT.get(i);
					if (enchantment != null && stack.canBeEnchantedWith(enchantment, EnchantingContext.RANDOM_ENCHANTMENT)) {
						entries.add(new EnchantmentLevelEntry(enchantment, 1));
						return;
					}
				}
			}
		}
	}
}
