/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isEnchanted();

	@SuppressWarnings("ConstantValue")
	@ModifyReturnValue(method = "getRarity", at = @At("RETURN"))
	private Rarity enchancement$singleLevelMode(Rarity original) {
		if (ModConfig.singleLevelMode && isEnchanted() && !EnchancementUtil.hasWeakEnchantments((ItemStack) (Object) this)) {
			return Rarity.values()[Math.min(original.ordinal() + 1, Rarity.values().length - 1)];
		}
		return original;
	}
}
