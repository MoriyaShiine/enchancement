/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.coyotebite;

import moriyashiine.enchancement.client.event.CoyoteBiteEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftClient.class, priority = 1001)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerInteractionManager interactionManager;

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
	private void enchancement$coyoteBite(CallbackInfoReturnable<Boolean> cir) {
		if (CoyoteBiteEvent.target != null) {
			interactionManager.attackEntity(player, CoyoteBiteEvent.target);
			player.swingHand(Hand.MAIN_HAND);
			cir.setReturnValue(true);
		}
	}
}
