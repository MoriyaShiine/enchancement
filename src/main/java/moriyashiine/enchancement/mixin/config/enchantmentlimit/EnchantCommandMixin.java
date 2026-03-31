/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {
	@ModifyExpressionValue(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;isEnchantmentCompatible(Ljava/util/Collection;Lnet/minecraft/core/Holder;)Z"))
	private static boolean enchancement$enchantmentLimit(boolean value, @Local(name = "item") ItemStack item) {
		return value && !EnchancementUtil.exceedsLimit(item, item.getEnchantments().size() + 1);
	}
}
