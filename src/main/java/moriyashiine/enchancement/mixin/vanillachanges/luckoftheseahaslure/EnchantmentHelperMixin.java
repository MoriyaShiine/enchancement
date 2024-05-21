/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.luckoftheseahaslure;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getLure", at = @At("RETURN"))
	private static int enchancement$luckOfTheSeaHasLure(int original, ItemStack stack) {
		if (ModConfig.luckOfTheSeaHasLure && EnchancementUtil.hasEnchantment(Enchantments.LUCK_OF_THE_SEA, stack)) {
			return EnchancementUtil.alterLevel(stack, Enchantments.LURE);
		}
		return original;
	}
}
