/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.weaponenchantmentcooldownrequirement;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyVariable(method = "onTargetDamaged", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$weaponEnchantmentCooldownRequirement(boolean runEnchantmentEffects) {
		return runEnchantmentEffects && SLibUtils.isAttackingPlayerCooldownWithinThreshold(ModConfig.weaponEnchantmentCooldownRequirement);
	}
}
