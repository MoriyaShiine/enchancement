/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@ModifyConstant(method = "renderArmWithItem", constant = @Constant(floatValue = 10, ordinal = 1))
	private float enchancement$rebalanceEquipment(float value, @Local(argsOnly = true) ItemStack itemStack) {
		return EnchancementUtil.getTridentChargeTime(itemStack);
	}
}
