/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DelayedLaunchComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
public class ProjectileMixin {
	@ModifyReturnValue(method = {
			"spawnProjectileFromRotation",
			"spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;",
			"spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"},
			at = @At("RETURN"))
	private static <T extends Projectile> T enchancement$delayedLaunch(T original, @Local(name = "pow") float pow, @Local(name = "uncertainty") float uncertainty) {
		if (original.getOwner() instanceof LivingEntity owner) {
			DelayedLaunchComponent.maybeSet(owner, owner.getActiveItem(), original, pow, uncertainty);
		}
		return original;
	}
}
