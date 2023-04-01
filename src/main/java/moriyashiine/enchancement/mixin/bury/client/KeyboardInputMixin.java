/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.bury.client;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
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
	private void enchancement$bury(boolean slowDown, float f, CallbackInfo ci) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && ModEntityComponents.BURY.get(player).getBuryPos() != null) {
			pressingForward = false;
			pressingBack = false;
			pressingLeft = false;
			pressingRight = false;
			movementForward = 0;
			movementSideways = 0;
		}
	}
}
