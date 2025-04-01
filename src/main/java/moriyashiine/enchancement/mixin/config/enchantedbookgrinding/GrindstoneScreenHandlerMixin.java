/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
	@ModifyReturnValue(method = "getOutputStack", at = @At("RETURN"))
	private ItemStack enchancement$enchantedBookGrinding(ItemStack original, ItemStack firstInput, ItemStack secondInput) {
		if (ModConfig.enchantedBookGrinding && firstInput.hasEnchantments() && secondInput.isOf(Items.BOOK)) {
			ItemStack book = Items.ENCHANTED_BOOK.getDefaultStack();
			EnchantmentHelper.set(book, firstInput.getEnchantments());
			return book;
		}
		return original;
	}
}
