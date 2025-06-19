/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.item.ItemRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements ItemRenderStateAddition {
	@Unique
	private int rageColor = -1;

	@Override
	public void enchancement$setRageColor(int color) {
		rageColor = color;
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void enchancement$rage(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		if (rageColor != -1) {
			RageEffect.color = rageColor;
		}
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void enchancement$rageTail(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		RageEffect.color = -1;
	}
}
