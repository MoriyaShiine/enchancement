/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnchantmentHelper.class, priority = 1001)
public class EnchantmentHelperMixin {
	@WrapOperation(method = "forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;accept(Lnet/minecraft/enchantment/Enchantment;I)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.Consumer instance, Enchantment enchantment, int i, Operation<Void> original, EnchantmentHelper.Consumer consumer, ItemStack stack) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.alterLevel(stack, enchantment);
		}
		original.call(instance, enchantment, i);
	}

	@ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
	private static int enchancement$singleLevelMode(int original, Enchantment enchantment, ItemStack stack) {
		if (original > 0 && ModConfig.singleLevelMode) {
			return EnchancementUtil.alterLevel(stack, enchantment);
		}
		return original;
	}
}
