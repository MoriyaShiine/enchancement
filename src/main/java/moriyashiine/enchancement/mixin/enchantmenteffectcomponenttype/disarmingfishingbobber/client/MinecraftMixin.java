/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	@Nullable
	public LocalPlayer player;

	@Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(CallbackInfoReturnable<Boolean> cir) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).isDisabled(player.getMainHandItem())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(CallbackInfo ci) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).isDisabled(player.getMainHandItem())) {
			ci.cancel();
		}
	}

	@Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(boolean down, CallbackInfo ci) {
		if (player != null && ModEntityComponents.DISARMED_PLAYER.get(player).isDisabled(player.getMainHandItem())) {
			ci.cancel();
		}
	}
}
