/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionMixin {
	@ModifyExpressionValue(method = "process", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getRandomOrEmpty(Ljava/util/List;Lnet/minecraft/util/math/random/Random;)Ljava/util/Optional;"))
	private Optional<RegistryEntry<Enchantment>> enchancement$disableDisallowedEnchantments(Optional<RegistryEntry<Enchantment>> original, ItemStack stack, LootContext context) {
		if (original.isEmpty() || !EnchancementUtil.isEnchantmentAllowed(original.get())) {
			return Optional.ofNullable(EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_RANDOM_LOOT, context.getRandom()));
		}
		return original;
	}
}
