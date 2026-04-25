/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
	public LocalPlayerMixin(ClientLevel level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@ModifyReturnValue(method = "itemUseSpeedMultiplier", at = @At("RETURN"))
	private float enchancement$rebalanceEquipment(float original) {
		if (ModConfig.rebalanceEquipment) {
			return MultiplyMovementSpeedEvent.getItemUseSpeedMultiplier(getUseItem(), original);
		}
		return original;
	}
}
