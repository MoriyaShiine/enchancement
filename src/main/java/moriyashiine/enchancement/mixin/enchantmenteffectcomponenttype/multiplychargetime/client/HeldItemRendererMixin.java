/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.multiplychargetime.client;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@ModifyVariable(method = "renderFirstPersonItem", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$multiplyChargeTime(float value, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item) {
		return value / EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME, player.getRandom(), item, 1);
	}
}
