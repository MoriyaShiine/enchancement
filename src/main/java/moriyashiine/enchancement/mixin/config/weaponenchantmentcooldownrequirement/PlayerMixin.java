/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.weaponenchantmentcooldownrequirement;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class PlayerMixin {
	@ModifyVariable(method = "itemAttackInteraction", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$weaponEnchantmentCooldownRequirement(boolean applyToTarget) {
		return applyToTarget && SLibUtils.isAttackingPlayerCooldownWithinThreshold(ModConfig.weaponEnchantmentCooldownRequirement);
	}
}
