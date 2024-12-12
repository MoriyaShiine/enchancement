/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.chargejump.client;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
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
	private void enchancement$chargeJump(CallbackInfoReturnable<Boolean> cir) {
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.getNullable(client.getCameraEntity());
		if (chargeJumpComponent != null && chargeJumpComponent.hasChargeJump() && chargeJumpComponent.getChargeProgress() > 0) {
			cir.setReturnValue(false);
		}
	}
}
