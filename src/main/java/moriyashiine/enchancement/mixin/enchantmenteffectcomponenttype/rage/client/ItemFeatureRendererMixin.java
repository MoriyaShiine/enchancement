/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.renderer.item.state.RageRenderState;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFeatureRenderer.class)
public class ItemFeatureRendererMixin {
	@Inject(method = "renderItem", at = @At("HEAD"))
	private void enchancement$rage(MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, SubmitNodeStorage.ItemSubmit submit, CallbackInfo ci) {
		RageEffect.color = ((RageRenderState.Submit) (Object) submit).enchancement$getColor();
	}

	@Inject(method = "renderItem", at = @At("TAIL"))
	private void enchancement$rage(CallbackInfo ci) {
		RageEffect.color = -1;
	}
}
