/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.render.item.RageRenderState;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.ItemCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCommandRenderer.class)
public class ItemCommandRendererMixin {
	@SuppressWarnings({"DataFlowIssue", "LocalMayBeArgsOnly"})
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void enchancement$rage(BatchingRenderCommandQueue queue, VertexConsumerProvider.Immediate vertexConsumers, OutlineVertexConsumerProvider outlineVertexConsumers, CallbackInfo ci, @Local OrderedRenderCommandQueueImpl.ItemCommand command) {
		RageEffect.color = ((RageRenderState.Command) (Object) command).enchancement$getColor();
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void enchancement$rage(BatchingRenderCommandQueue queue, VertexConsumerProvider.Immediate vertexConsumers, OutlineVertexConsumerProvider outlineVertexConsumers, CallbackInfo ci) {
		RageEffect.color = -1;
	}
}
