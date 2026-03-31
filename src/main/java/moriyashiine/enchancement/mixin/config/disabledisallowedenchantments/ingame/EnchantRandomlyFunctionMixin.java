/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {
	@ModifyExpressionValue(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getRandomSafe(Ljava/util/List;Lnet/minecraft/util/RandomSource;)Ljava/util/Optional;"))
	private Optional<Holder<Enchantment>> enchancement$disableDisallowedEnchantments(Optional<Holder<Enchantment>> original, ItemStack itemStack, LootContext context) {
		if (original.isEmpty() || !EnchancementUtil.isEnchantmentAllowed(original.get())) {
			return Optional.ofNullable(EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_RANDOM_LOOT, context.getRandom()));
		}
		return original;
	}
}
