/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$slide(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(player);
			if (slideComponent.isSliding()) {
				if (slideComponent.getVelocity().x() != 0) {
					pressingForward = pressingBack = false;
					movementForward = 0;
				}
				if (slideComponent.getVelocity().z() != 0) {
					pressingLeft = pressingRight = false;
					movementSideways = 0;
				}
			}
		}
	}
}
