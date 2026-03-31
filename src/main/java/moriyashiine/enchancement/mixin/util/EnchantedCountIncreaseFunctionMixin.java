/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantedCountIncreaseFunction.class)
public class EnchantedCountIncreaseFunctionMixin {
	@Shadow
	@Final
	private Holder<Enchantment> enchantment;

	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"))
	private int enchancement$fixHardcodedLooting(Holder<Enchantment> enchantment, LivingEntity entity, Operation<Integer> original, ItemStack itemStack) {
		int level = original.call(enchantment, entity);
		Registry<Enchantment> registry = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		for (Enchantment object : registry) {
			Holder<Enchantment> entry = registry.wrapAsHolder(object);
			if (entry.is(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS) && !entry.equals(this.enchantment)) {
				level += EnchantmentHelper.getEnchantmentLevel(entry, entity);
			}
		}
		return level;
	}
}
