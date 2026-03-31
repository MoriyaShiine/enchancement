/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.toggleablepassives;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
	@Inject(method = "removeNonCursesFrom", at = @At("TAIL"))
	private void enchancement$toggleablePassives(ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
		if (!item.isEnchanted() && item.has(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			item.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
		}
	}
}
