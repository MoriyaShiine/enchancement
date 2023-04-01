/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.luckoftheseahaslure;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getLure", at = @At("HEAD"), cancellable = true)
	private static void enchancement$luckOfTheSeaHasLure(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (ModConfig.luckOfTheSeaHasLure && EnchancementUtil.hasEnchantment(Enchantments.LUCK_OF_THE_SEA, stack)) {
			cir.setReturnValue(Enchantments.LURE.getMaxLevel());
		}
	}
}
