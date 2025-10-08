/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@ModifyVariable(method = "canSprint(Z)Z", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$fluidWalkingStart(boolean original) {
		return original || EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}

	@ModifyExpressionValue(method = "shouldStopSwimSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isTouchingWater()Z"))
	private boolean enchancement$fluidWalkingStop(boolean original) {
		return original && !EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
