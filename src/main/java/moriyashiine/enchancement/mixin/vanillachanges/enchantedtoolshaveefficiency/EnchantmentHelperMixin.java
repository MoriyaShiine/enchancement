/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantedtoolshaveefficiency;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
	private static int enchancement$enchantedToolsHaveEfficiency(int original, Enchantment enchantment, ItemStack stack) {
		if (ModConfig.enchantedToolsHaveEfficiency && enchantment == Enchantments.EFFICIENCY && stack.isIn(ItemTags.MINING_ENCHANTABLE) && stack.getOrDefault(ModDataComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!stack.hasEnchantments()) {
				stack.remove(ModDataComponentTypes.TOGGLEABLE_PASSIVE);
				return original;
			}
			return enchantment.getMaxLevel();
		}
		return original;
	}
}
