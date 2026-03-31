/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
	public LocalPlayerMixin(ClientLevel level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@ModifyVariable(method = "isSprintingPossible", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$fluidWalkingStart(boolean allowedInShallowWater) {
		return allowedInShallowWater || EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}

	@ModifyExpressionValue(method = "shouldStopSwimSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
	private boolean enchancement$fluidWalkingStop(boolean original) {
		return original && !EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
