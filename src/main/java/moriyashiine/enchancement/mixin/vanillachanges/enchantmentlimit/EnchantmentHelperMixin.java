/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static ItemStack cachedStack = null;

	@ModifyReturnValue(method = "generateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static List<EnchantmentLevelEntry> enchancement$enchantmentLimit(List<EnchantmentLevelEntry> original, FeatureSet enabledFeatures, Random random, ItemStack stack) {
		for (int i = original.size() - 1; i >= 0; i--) {
			if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, original.size()) > ModConfig.enchantmentLimit)) {
				original.remove(i);
			}
		}
		return original;
	}

	@Inject(method = "set", at = @At("HEAD"))
	private static void enchancement$enchantmentLimit(ItemStack stack, ItemEnchantmentsComponent enchantments, CallbackInfo ci) {
		cachedStack = stack;
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantmentsComponent enchancement$enchantmentLimit(ItemEnchantmentsComponent value) {
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(value);
		builder.remove(enchantment -> !EnchancementUtil.limitCheck(true, EnchancementUtil.getNonDefaultEnchantmentsSize(cachedStack, value.getEnchantments().size()) < ModConfig.enchantmentLimit));
		cachedStack = null;
		return builder.build();
	}
}
