/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
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
	private Minecraft minecraft;

	@Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
	private void enchancement$slide(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo ci) {
		if (minecraft.player != null && ModEntityComponents.SLIDE.get(minecraft.player).isSliding()) {
			ci.cancel();
		}
	}
}
