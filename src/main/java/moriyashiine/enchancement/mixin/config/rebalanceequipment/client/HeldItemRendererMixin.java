/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@ModifyConstant(method = "renderFirstPersonItem", constant = @Constant(floatValue = 10, ordinal = 1))
	private float enchancement$rebalanceEquipment(float value) {
		if (ModConfig.rebalanceEquipment) {
			return 20;
		}
		return value;
	}
}
