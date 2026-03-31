/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SetEnchantmentsFunction.class)
public class SetEnchantmentsFunctionMixin {
	@Shadow
	@Final
	@Mutable
	private Map<Holder<Enchantment>, NumberProvider> enchantments;

	@Inject(method = "run", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(ItemStack itemStack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		boolean[] anyDisallowed = {false};
		enchantments.forEach((enchantment, _) -> {
			if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				anyDisallowed[0] = true;
			}
		});
		if (anyDisallowed[0]) {
			Map<Holder<Enchantment>, NumberProvider> newEnchantments = new HashMap<>();
			enchantments.forEach((enchantment, _) -> {
				if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
					newEnchantments.put(EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_RANDOM_LOOT, context.getRandom()), ConstantValue.exactly(1));
				}
			});
			enchantments = newEnchantments;
		}
	}
}
