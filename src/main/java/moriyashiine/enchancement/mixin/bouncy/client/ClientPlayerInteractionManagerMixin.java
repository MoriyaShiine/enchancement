/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.bouncy.client;

import moriyashiine.enchancement.common.component.entity.BouncyComponent;
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

	@Inject(method = "hasExperienceBar", at = @At("HEAD"), cancellable = true)
	private void enchancement$bouncy(CallbackInfoReturnable<Boolean> cir) {
		BouncyComponent bouncyComponent = ModEntityComponents.BOUNCY.getNullable(client.getCameraEntity());
		if (bouncyComponent != null && bouncyComponent.hasBouncy() && bouncyComponent.getBoostProgress() > 0) {
			cir.setReturnValue(false);
		}
	}
}
