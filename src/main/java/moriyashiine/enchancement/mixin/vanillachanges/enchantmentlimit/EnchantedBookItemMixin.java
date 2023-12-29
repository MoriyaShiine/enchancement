/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private static void enchancement$enchantmentLimit(ItemStack stack, EnchantmentLevelEntry entry, CallbackInfo ci) {
		if (EnchancementUtil.limitCheck(false, stack.getEnchantments().size() >= ModConfig.enchantmentLimit)) {
			ci.cancel();
		}
	}
}
