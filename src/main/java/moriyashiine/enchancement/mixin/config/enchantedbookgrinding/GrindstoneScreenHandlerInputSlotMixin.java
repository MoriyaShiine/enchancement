/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$3")
public class GrindstoneScreenHandlerInputSlotMixin {
	@ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
	private boolean enchancement$enchantedBookGrinding(boolean original, ItemStack stack) {
		return original || (ModConfig.enchantedBookGrinding && stack.isOf(Items.BOOK));
	}
}
