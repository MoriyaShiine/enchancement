/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.fasterbows;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BowItem.class)
public class BowItemMixin {
	@ModifyVariable(method = "getPullProgress", at = @At(value = "STORE", ordinal = 0))
	private static float enchancement$fasterBows(float value) {
		if (ModConfig.fasterBows) {
			return value * 1.5F;
		}
		return value;
	}
}
