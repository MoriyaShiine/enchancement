/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FabricItem.class)
public interface FabricItemMixin {
	@ModifyReturnValue(method = "canBeEnchantedWith", at = @At("RETURN"))
	private boolean enchancement$disableDisallowedEnchantments(boolean original, ItemStack stack, Enchantment enchantment) {
		if (!enchantment.isEnabled(enchantment.getRequiredFeatures())) {
			return false;
		}
		return original;
	}
}
