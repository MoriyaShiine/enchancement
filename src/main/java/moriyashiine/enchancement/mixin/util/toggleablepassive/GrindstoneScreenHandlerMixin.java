/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.toggleablepassive;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
	@Inject(method = "grind", at = @At("TAIL"))
	private void enchancement$toggleablePassive(ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
		if (!item.hasEnchantments() && item.contains(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			item.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
		}
	}
}
