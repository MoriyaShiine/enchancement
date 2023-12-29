/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.client.packet.StopBrimstoneSoundsS2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Unique
	private int slot = -1;

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;", shift = At.Shift.BEFORE))
	private void enchancement$brimstoneBefore(CallbackInfo ci) {
		if (slot == -1 && player != null) {
			slot = player.getInventory().selectedSlot;
		}
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;", shift = At.Shift.BY, by = 3))
	private void enchancement$brimstoneAfter(CallbackInfo ci) {
		if (slot != -1 && player != null) {
			PlayerInventory inventory = player.getInventory();
			if (slot != inventory.selectedSlot) {
				StopBrimstoneSoundsS2CPacket.maybeStopSounds(player, inventory.main.get(slot));
			}
			slot = -1;
		}
	}
}
