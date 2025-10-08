/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.item.RageRenderState;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin {
	@Inject(method = "render", at = @At("HEAD"))
	private void enchancement$rage(MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, int overlay, int i, CallbackInfo ci) {
		int color = -1;
		@Nullable RageRenderState rageRenderState = ((FabricRenderState) this).getData(RageRenderState.KEY);
		if (rageRenderState != null) {
			color = rageRenderState.color;
		}
		RageEffect.color = color;
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void enchancement$rageTail(MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, int overlay, int i, CallbackInfo ci) {
		RageEffect.color = -1;
	}
}
