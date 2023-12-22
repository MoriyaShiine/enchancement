/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@Unique
	private static Enchantment replacement = null;

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private static void enchancement$disableDisallowedEnchantments(ItemStack stack, EnchantmentLevelEntry entry, CallbackInfo ci) {
		if (!EnchancementUtil.isEnchantmentAllowed(entry.enchantment)) {
			replacement = EnchancementUtil.getReplacement(entry.enchantment, stack);
			if (replacement == null) {
				ci.cancel();
			}
		}
	}

	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private static EnchantmentLevelEntry enchancement$disableDisallowedEnchantments(EnchantmentLevelEntry value, ItemStack stack) {
		if (replacement != null) {
			Enchantment temp = replacement;
			replacement = null;
			return new EnchantmentLevelEntry(temp, Math.min(temp.getMaxLevel(), value.level));
		}
		return value;
	}
}
