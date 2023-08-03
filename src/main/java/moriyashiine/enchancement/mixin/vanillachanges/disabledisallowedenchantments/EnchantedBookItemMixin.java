/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private static EnchantmentLevelEntry enchancement$disableDisallowedEnchantments(EnchantmentLevelEntry value, ItemStack stack) {
		if (EnchancementUtil.isEnchantmentAllowed(value.enchantment)) {
			return value;
		}
		return new EnchantmentLevelEntry(EnchancementUtil.getReplacement(value.enchantment, stack), value.level);
	}
}
