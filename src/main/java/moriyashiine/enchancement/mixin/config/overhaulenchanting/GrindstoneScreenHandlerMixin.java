/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
	@ModifyReturnValue(method = "getOutputStack", at = @At("RETURN"))
	private ItemStack enchancement$overhaulEnchanting(ItemStack original, ItemStack firstInput, ItemStack secondInput) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && firstInput.hasEnchantments() && secondInput.isOf(Items.BOOK)) {
			ItemStack book = Items.ENCHANTED_BOOK.getDefaultStack();
			EnchantmentHelper.set(book, firstInput.getEnchantments());
			return book;
		}
		return original;
	}
}
