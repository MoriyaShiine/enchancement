/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$3")
public class GrindstoneMenuInputSlotMixin {
	@ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
	private boolean enchancement$overhaulEnchanting(boolean original, ItemStack itemStack) {
		return original || (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && itemStack.is(Items.BOOK));
	}
}
