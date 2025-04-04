/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {
	@Shadow
	@Final
	@Mutable
	private Map<RegistryEntry<Enchantment>, LootNumberProvider> enchantments;

	@Inject(method = "process", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		boolean[] allowed = {true};
		enchantments.forEach((enchantment, level) -> {
			if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				allowed[0] = false;
			}
		});
		if (!allowed[0]) {
			Map<RegistryEntry<Enchantment>, LootNumberProvider> newEnchantments = new HashMap<>();
			enchantments.forEach((enchantment, level) -> {
				if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
					newEnchantments.put(EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_RANDOM_LOOT, context.getRandom()), ConstantLootNumberProvider.create(1));
				}
			});
			enchantments = newEnchantments;
		}
	}
}
