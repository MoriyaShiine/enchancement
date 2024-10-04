/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "apply", at = @At("HEAD"), cancellable = true)
	private static void enchancement$enchantmentLimit(ItemStack stack, Consumer<ItemEnchantmentsComponent.Builder> applier, CallbackInfoReturnable<ItemEnchantmentsComponent> cir) {
		if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, stack.getEnchantments().getSize()) >= ModConfig.enchantmentLimit)) {
			cir.setReturnValue(stack.getEnchantments());
		}
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantmentsComponent enchancement$enchantmentLimit(ItemEnchantmentsComponent value, ItemStack stack) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>(value.getEnchantments());
		while (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, enchantments.size()) > ModConfig.enchantmentLimit)) {
			enchantments.removeFirst();
		}
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(value);
		builder.remove(enchantment -> !enchantments.contains(enchantment));
		return builder.build();
	}

	@ModifyReturnValue(method = "generateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static List<EnchantmentLevelEntry> enchancement$enchantmentLimit(List<EnchantmentLevelEntry> original, Random random, ItemStack stack) {
		for (int i = original.size() - 1; i >= 0; i--) {
			if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, original.size()) > ModConfig.enchantmentLimit)) {
				original.remove(i);
			}
		}
		return original;
	}
}
