/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyVariable(method = "shootAll", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$rebalanceProjectiles(float value, ServerWorld world, LivingEntity shooter) {
		if (ModConfig.rebalanceProjectiles && shooter instanceof PlayerEntity) {
			return value * 1.5F;
		}
		return value;
	}
}
