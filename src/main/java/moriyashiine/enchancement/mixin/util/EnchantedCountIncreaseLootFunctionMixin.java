/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantedCountIncreaseLootFunction.class)
public class EnchantedCountIncreaseLootFunctionMixin {
	@Shadow
	@Final
	private RegistryEntry<Enchantment> enchantment;

	@WrapOperation(method = "process", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEquipmentLevel(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/LivingEntity;)I"))
	private int enchancement$fixHardcodedLooting(RegistryEntry<Enchantment> enchantmentEntry, LivingEntity entity, Operation<Integer> original, ItemStack stack) {
		int value = original.call(enchantmentEntry, entity);
		Registry<Enchantment> registry = entity.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
		for (Enchantment enchantment : registry) {
			RegistryEntry<Enchantment> entry = registry.getEntry(enchantment);
			if (entry.isIn(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS) && !entry.equals(this.enchantment)) {
				value += EnchantmentHelper.getEquipmentLevel(entry, entity);
			}
		}
		return value;
	}
}
