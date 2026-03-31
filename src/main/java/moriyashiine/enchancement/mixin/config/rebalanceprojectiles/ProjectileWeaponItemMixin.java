/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceprojectiles;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
	@ModifyVariable(method = "shoot", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float enchancement$rebalanceProjectiles(float power, ServerLevel level, LivingEntity shooter) {
		if (ModConfig.rebalanceProjectiles && shooter instanceof Player) {
			return power * 1.25F;
		}
		return power;
	}
}
