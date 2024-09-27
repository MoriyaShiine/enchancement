/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantedtridentshaveloyalty;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getTridentReturnAcceleration", at = @At("RETURN"))
	private static int enchancement$enchantedTridentsHaveLoyalty(int original, ServerWorld world, ItemStack stack) {
		if (ModConfig.enchantedTridentsHaveLoyalty && stack.isIn(ItemTags.TRIDENT_ENCHANTABLE) && !stack.isIn(ModItemTags.NO_LOYALTY) && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!stack.hasEnchantments()) {
				stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				return original;
			}
			return EnchancementUtil.hasWeakEnchantments(stack) ? 1 : 3;
		}
		return original;
	}
}
