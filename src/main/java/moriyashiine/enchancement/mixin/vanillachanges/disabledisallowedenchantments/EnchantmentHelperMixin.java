/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyVariable(method = "set", at = @At(value = "HEAD"), argsOnly = true)
	private static Map<Enchantment, Integer> enchancement$disableDisallowedEnchantments(Map<Enchantment, Integer> value, Map<Enchantment, Integer> map, ItemStack stack) {
		Map<Enchantment, Integer> newMap = new LinkedHashMap<>();
		for (Enchantment enchantment : value.keySet()) {
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				newMap.put(enchantment, value.get(enchantment));
			} else {
				Enchantment replacement = EnchancementUtil.getReplacement(enchantment, stack);
				if (replacement != null) {
					newMap.put(replacement, Math.min(replacement.getMaxLevel(), value.get(enchantment)));
				}
			}
		}
		return newMap;
	}
}
