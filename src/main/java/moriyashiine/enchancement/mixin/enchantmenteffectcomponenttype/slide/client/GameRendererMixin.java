/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
	private void enchancement$slide(MatrixStack matrices, float tickProgress, CallbackInfo ci) {
		if (client.player != null && ModEntityComponents.SLIDE.get(client.player).isSliding()) {
			ci.cancel();
		}
	}
}
