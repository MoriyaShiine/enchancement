/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disablevelocitychecks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@ModifyReturnValue(method = "shouldCheckMovement", at = @At("RETURN"))
	public boolean enchancement$disableVelocityChecksQuickly(boolean value) {
		return value && !ModConfig.disableVelocityChecks;
	}

	@ModifyExpressionValue(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isInTeleportationState()Z"))
	public boolean enchancement$disableVelocityChecksWrongly(boolean value) {
		if (ModConfig.disableVelocityChecks) {
			return true;
		}
		return value;
	}
}
