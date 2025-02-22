/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.HoeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HoeItem.class)
public class HoeItemMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceEquipmentDamage(float value) {
		return ModConfig.rebalanceEquipment ? Math.max(0, value) + 1 : value;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private static float enchancement$rebalanceEquipmentSpeed(float value) {
		return ModConfig.rebalanceEquipment ? -2 : value;
	}
}
