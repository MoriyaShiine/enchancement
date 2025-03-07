/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyReturnValue(method = "createArrowEntity", at = @At("RETURN"))
	private ProjectileEntity enchancement$allowLoadingProjectile(ProjectileEntity original, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
		Set<Item> items = AllowLoadingProjectileEffect.getItems(weaponStack);
		for (Item item : items) {
			if (projectileStack.isOf(item) || !(shooter instanceof PlayerEntity)) {
				AllowLoadingProjectileEffect.cachedSoundEvent = AllowLoadingProjectileEffect.getSoundEvent(weaponStack, projectileStack.getItem());
				ProjectileEntity projectile = AllowLoadingProjectileEffect.PROJECTILE_MAP.get(item).getProjectile(world, shooter, projectileStack, weaponStack);
				if (critical && projectile instanceof PersistentProjectileEntity persistentProjectile) {
					persistentProjectile.setCritical(true);
				}
				return projectile;
			}
		}
		return original;
	}
}
