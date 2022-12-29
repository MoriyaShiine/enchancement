/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static final Random RANDOM = new Random();

	@ModifyVariable(method = "set", at = @At(value = "HEAD"), argsOnly = true)
	private static Map<Enchantment, Integer> enchancement$disableDisallowedEnchantments(Map<Enchantment, Integer> value, Map<Enchantment, Integer> map, ItemStack stack) {
		HashMap<Enchantment, Integer> newEnchantments = new LinkedHashMap<>();
		for (Enchantment enchantment : value.keySet()) {
			if (ModConfig.isEnchantmentAllowed(Registries.ENCHANTMENT.getId(enchantment))) {
				newEnchantments.put(enchantment, ModConfig.singleLevelMode ? 1 : value.get(enchantment));
			}
		}
		if (newEnchantments.isEmpty() && stack.isOf(Items.ENCHANTED_BOOK)) {
			Enchantment enchantment = null;
			while (enchantment == null || !ModConfig.isEnchantmentAllowed(Registries.ENCHANTMENT.getId(enchantment))) {
				enchantment = Registries.ENCHANTMENT.get(RANDOM.nextInt(Registries.ENCHANTMENT.size()));
			}
			newEnchantments.put(enchantment, 1);
		}
		return newEnchantments;
	}
}
