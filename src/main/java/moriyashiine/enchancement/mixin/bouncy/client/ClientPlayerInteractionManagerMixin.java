/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.bouncy.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "hasExperienceBar", at = @At("RETURN"), cancellable = true)
	private void enchancement$bouncy(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ()) {
			ModEntityComponents.BOUNCY.maybeGet(client.getCameraEntity()).ifPresent(bouncyComponent -> {
				if (bouncyComponent.hasBouncy() && bouncyComponent.getBoostProgress() > 0) {
					cir.setReturnValue(false);
				}
			});
		}
	}
}
