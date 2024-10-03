/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantedtoolshaveefficiency;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static void checkPassive(ItemStack stack, ItemEnchantmentsComponent enchantmentsComponent) {
		if (ModConfig.enchantedToolsHaveEfficiency && stack.isIn(ItemTags.MINING_ENCHANTABLE)) {
			if (stack.hasEnchantments()) {
				if (!stack.contains(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
					for (RegistryEntry<Enchantment> enchantment : enchantmentsComponent.getEnchantments()) {
						if (enchantment.isIn(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)) {
							return;
						}
					}
					stack.set(ModComponentTypes.TOGGLEABLE_PASSIVE, true);
				}
			} else {
				stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
			}
		}
	}

	@Inject(method = "apply", at = @At(value = "RETURN", ordinal = 1))
	private static void enchancement$enchantedToolsHaveEfficiency(ItemStack stack, Consumer<ItemEnchantmentsComponent.Builder> applier, CallbackInfoReturnable<ItemEnchantmentsComponent> cir, @Local(ordinal = 1) ItemEnchantmentsComponent enchantments) {
		checkPassive(stack, enchantments);
	}

	@Inject(method = "set", at = @At("TAIL"))
	private static void enchancement$enchantedToolsHaveEfficiency(ItemStack stack, ItemEnchantmentsComponent enchantments, CallbackInfo ci) {
		checkPassive(stack, enchantments);
	}
}
