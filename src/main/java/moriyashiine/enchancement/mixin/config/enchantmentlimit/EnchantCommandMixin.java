/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.EnchantCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {
	@ModifyExpressionValue(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;isCompatible(Ljava/util/Collection;Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
	private static boolean enchancement$enchantmentLimit(boolean value, @Local ItemStack stack) {
		return value && !EnchancementUtil.exceedsLimit(stack, stack.getEnchantments().getSize() + 1);
	}
}
