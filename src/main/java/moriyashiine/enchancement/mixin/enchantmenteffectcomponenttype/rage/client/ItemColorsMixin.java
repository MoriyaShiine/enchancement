/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemColors.class)
public class ItemColorsMixin {
	@ModifyReturnValue(method = "getColor", at = @At("RETURN"))
	private int enchancement$rage(int original) {
		int color = RageEffect.color;
		if (color != -1) {
			if (original != -1) {
				color *= original;
			}
			return color;
		}
		return original;
	}
}
