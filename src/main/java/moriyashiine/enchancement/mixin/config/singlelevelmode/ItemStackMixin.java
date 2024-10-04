/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean hasEnchantments();

	@ModifyReturnValue(method = "getRarity", at = @At("RETURN"))
	private Rarity enchancement$singleLevelMode(Rarity original) {
		if (ModConfig.singleLevelMode && hasEnchantments() && !EnchancementUtil.hasWeakEnchantments((ItemStack) (Object) this)) {
			return Rarity.values()[Math.min(original.ordinal() + 1, Rarity.values().length - 1)];
		}
		return original;
	}
}
