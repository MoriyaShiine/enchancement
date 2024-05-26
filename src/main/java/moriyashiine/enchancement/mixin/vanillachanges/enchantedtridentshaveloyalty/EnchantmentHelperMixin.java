/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantedtridentshaveloyalty;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getLoyalty", at = @At("RETURN"))
	private static int enchancement$enchantedTridentsHaveLoyalty(int original, ItemStack stack) {
		if (ModConfig.enchantedTridentsHaveLoyalty && stack.isIn(ItemTags.TRIDENT_ENCHANTABLE) && !stack.isIn(ModTags.Items.NO_LOYALTY) && stack.getOrDefault(ModDataComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!stack.hasEnchantments()) {
				stack.remove(ModDataComponentTypes.TOGGLEABLE_PASSIVE);
				return original;
			}
			return EnchancementUtil.alterLevel(stack, Enchantments.LOYALTY);
		}
		return original;
	}
}
