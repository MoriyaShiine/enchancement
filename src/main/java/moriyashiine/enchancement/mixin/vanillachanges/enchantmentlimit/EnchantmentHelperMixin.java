/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "generateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static void enchancement$enchantmentLimit(Random random, ItemStack stack, int level, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		for (int i = cir.getReturnValue().size() - 1; i >= 0; i--) {
			if (EnchancementUtil.limitCheck(false, cir.getReturnValue().size() > ModConfig.enchantmentLimit)) {
				cir.getReturnValue().remove(i);
			}
		}
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static Map<Enchantment, Integer> enchancement$enchantmentLimit(Map<Enchantment, Integer> value) {
		Map<Enchantment, Integer> newMap = new LinkedHashMap<>();
		for (Enchantment enchantment : value.keySet()) {
			if (EnchancementUtil.limitCheck(true, newMap.size() < ModConfig.enchantmentLimit)) {
				newMap.put(enchantment, value.get(enchantment));
			}
		}
		return newMap;
	}
}
