/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disablevelocitychecks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;

	@ModifyExpressionValue(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"))
	public boolean enchancement$disableVelocityChecksQuickly(boolean value) {
		if (ModConfig.disableVelocityChecks) {
			return true;
		}
		return value;
	}

	@ModifyExpressionValue(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isInTeleportationState()Z", ordinal = 1))
	public boolean enchancement$disableVelocityChecksWrongly(boolean value) {
		if (ModConfig.disableVelocityChecks) {
			return true;
		}
		return value;
	}
}
