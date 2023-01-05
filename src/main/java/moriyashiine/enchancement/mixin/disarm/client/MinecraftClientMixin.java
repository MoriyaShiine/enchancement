/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.disarm.client;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
	private void enchancement$cooldownPreventsUsage(CallbackInfoReturnable<Boolean> cir) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).getDisarmedItems().contains(player.getMainHandStack().getItem())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
	private void enchancement$cooldownPreventsUsage(CallbackInfo ci) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).getDisarmedItems().contains(player.getMainHandStack().getItem())) {
			ci.cancel();
		}
	}

	@Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
	private void enchancement$cooldownPreventsUsage(boolean bl, CallbackInfo ci) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).getDisarmedItems().contains(player.getMainHandStack().getItem())) {
			ci.cancel();
		}
	}
}
