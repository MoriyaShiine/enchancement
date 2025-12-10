/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyExpressionValue(method = "useAttackEnchantmentEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canSprintOrFly()Z"))
	private boolean enchancement$rebalanceEnchantments(boolean original) {
		return original || ModConfig.rebalanceEnchantments;
	}
}
