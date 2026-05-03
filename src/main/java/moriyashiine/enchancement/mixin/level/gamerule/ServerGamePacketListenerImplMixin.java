/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.level.gamerule;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow
	public ServerPlayer player;

	@Shadow
	protected abstract boolean shouldCheckPlayerMovement(boolean isFallFlying);

	@ModifyVariable(method = {"handleMovePlayer", "handleMoveVehicle"}, at = @At("STORE"), name = "movedDist")
	private double enchancement$gameRule(double movedDist) {
		if (!player.isSleeping() && !shouldCheckPlayerMovement(false)) {
			return 0;
		}
		return movedDist;
	}

	@ModifyReturnValue(method = "getMaximumFlyingTicks", at = @At("RETURN"))
	private int enchancement$gameRule(int original) {
		return shouldCheckPlayerMovement(false) ? original : Integer.MAX_VALUE;
	}
}
