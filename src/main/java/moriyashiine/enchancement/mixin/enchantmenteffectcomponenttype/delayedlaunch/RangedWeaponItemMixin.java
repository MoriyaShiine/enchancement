/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.DelayedLaunchComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyExpressionValue(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;createArrowEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private ProjectileEntity enchancement$delayedLaunch(ProjectileEntity original, ServerWorld world, LivingEntity shooter, Hand hand, ItemStack weaponStack, List<ItemStack> projectiles, float speed, float divergence) {
		DelayedLaunchComponent.maybeSet(shooter, weaponStack, original, speed, divergence);
		return original;
	}
}
