/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting.client;

import moriyashiine.enchancement.client.gui.screen.EnchantingTableScreen;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
	@Inject(method = "getEntityBlockLayer", at = @At("HEAD"), cancellable = true)
	private static void enchancement$overhaulEnchanting(BlockState state, CallbackInfoReturnable<RenderLayer> cir) {
		if (EnchantingTableScreen.forceTransparency) {
			cir.setReturnValue(TexturedRenderLayers.getItemEntityTranslucentCull());
		}
	}
}
