/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
			return EnchancementUtil.getItemUseSpeedMultiplier(getUseItem(), original);
		}
		return original;
	}

	@ModifyExpressionValue(method = "isSlowDueToUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/UseEffects;canSprint()Z"))
	private boolean enchancement$rebalanceEquipment(boolean original) {
		return original || EnchancementUtil.isFastItem(getUseItem());
	}
}
