/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Unique
	private static Enchantment replacement = null;

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(Enchantment enchantment, int level, CallbackInfo ci) {
		if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			replacement = EnchancementUtil.getReplacement(enchantment, (ItemStack) (Object) this);
			if (replacement == null) {
				ci.cancel();
			}
		}
	}

	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private Enchantment enchancement$disableDisallowedEnchantments(Enchantment value) {
		if (replacement != null) {
			return replacement;
		}
		return value;
	}

	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private int enchancement$disableDisallowedEnchantments(int value) {
		if (replacement != null) {
			Enchantment temp = replacement;
			replacement = null;
			return Math.min(temp.getMaxLevel(), value);
		}
		return value;
	}
}
