/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
	@ModifyReturnValue(method = "createProjectile", at = @At("RETURN"))
	private Projectile enchancement$allowLoadingProjectile(Projectile original, Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
		Set<Item> items = AllowLoadingProjectileEffect.getItems(weapon);
		for (Item item : items) {
			if (projectile.is(item) || !(shooter instanceof Player)) {
				AllowLoadingProjectileEffect.cachedSoundEvent = AllowLoadingProjectileEffect.getSoundEvent(weapon, projectile.getItem());
				Projectile projectileEntity = AllowLoadingProjectileEffect.PROJECTILE_MAP.get(item).getProjectile(level, shooter, projectile, weapon);
				if (isCrit && projectileEntity instanceof AbstractArrow arrow) {
					arrow.setCritArrow(true);
				}
				return projectileEntity;
			}
		}
		return original;
	}
}
