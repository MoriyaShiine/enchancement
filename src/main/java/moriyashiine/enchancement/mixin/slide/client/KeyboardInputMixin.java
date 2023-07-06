/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$slide(boolean slowDown, float f, CallbackInfo ci) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && ModEntityComponents.SLIDE.get(player).isSliding()) {
			pressingForward = false;
			pressingBack = false;
			movementForward = 0;
		}
	}
}
