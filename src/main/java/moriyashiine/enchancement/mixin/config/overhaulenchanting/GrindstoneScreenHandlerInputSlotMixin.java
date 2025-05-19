/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$3")
public class GrindstoneScreenHandlerInputSlotMixin {
	@ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
	private boolean enchancement$overhaulEnchanting(boolean original, ItemStack stack) {
		return original || (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && stack.isOf(Items.BOOK));
	}
}
