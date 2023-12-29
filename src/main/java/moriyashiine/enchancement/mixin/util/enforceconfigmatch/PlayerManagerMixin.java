/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.util.enforceconfigmatch;

import moriyashiine.enchancement.client.packet.EnforceConfigMatchPacket;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER))
	private void enchancement$enforceConfigMatch(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		EnforceConfigMatchPacket.send(player, ModConfig.encode());
	}
}
