/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disablevelocitychecks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
	@ModifyReturnValue(method = "shouldCheckPlayerMovement", at = @At("RETURN"))
	public boolean enchancement$disableVelocityChecksQuickly(boolean value) {
		return value && !ModConfig.disableVelocityChecks;
	}

	@ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isChangingDimension()Z"))
	public boolean enchancement$disableVelocityChecksWrongly(boolean value) {
		if (ModConfig.disableVelocityChecks) {
			return true;
		}
		return value;
	}
}
