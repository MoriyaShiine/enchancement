/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
	@Shadow
	private Entity focusedEntity;

	@Shadow
	private float cameraY;

	@Inject(method = "updateEyeHeight", at = @At("TAIL"))
	private void enchancement$slide(CallbackInfo ci) {
		if (focusedEntity != null) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(focusedEntity);
			if (slideComponent != null && slideComponent.isSliding()) {
				cameraY += 0.1F;
			}
		}
	}
}
