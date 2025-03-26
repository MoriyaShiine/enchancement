/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.multiplychargetime.client;

import moriyashiine.enchancement.common.entity.UseTimeProgressHolder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@ModifyVariable(method = "renderFirstPersonItem", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$multiplyChargeTime(float value, AbstractClientPlayerEntity player) {
		return value + ((UseTimeProgressHolder) player).enchancement$getUseTimeProgress();
	}
}
