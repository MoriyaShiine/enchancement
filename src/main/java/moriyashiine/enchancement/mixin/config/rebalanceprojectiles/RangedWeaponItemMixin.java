/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyVariable(method = "shootAll", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$rebalanceProjectiles(float value) {
		if (ModConfig.rebalanceProjectiles) {
			return value * 1.5F;
		}
		return value;
	}
}
