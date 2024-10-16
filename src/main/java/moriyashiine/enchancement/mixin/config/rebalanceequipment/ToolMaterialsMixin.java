/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ToolMaterials.class)
public class ToolMaterialsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceEquipment(float value, String id) {
		if (ModConfig.rebalanceEquipment) {
			switch (id) {
				case "GOLD" -> {
					return 8;
				}
				case "DIAMOND" -> {
					return 9;
				}
				case "NETHERITE" -> {
					return 12;
				}
			}
		}
		return value;
	}
}
