/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.scattershot;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.enchantment.effect.ScatterShotEffect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {
	@Shadow
	protected abstract void shootAll(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target);

	@ModifyVariable(method = "createArrowEntity", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$scatterShot(boolean value, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (shouldApply(shooter, weaponStack, projectileStack)) {
			return false;
		}
		return value;
	}

	@ModifyVariable(method = "shootAll", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private float enchancement$scatterShot(float value, ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles) {
		if (ScatterShotEffect.hasScatterShot) {
			return 16;
		}
		return value;
	}

	@WrapOperation(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;createArrowEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private ProjectileEntity enchancement$scatterShot(RangedWeaponItem instance, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, Operation<ProjectileEntity> original) {
		ProjectileEntity projectile = original.call(instance, world, shooter, weaponStack, projectileStack, critical);
		if (ScatterShotEffect.hasScatterShot && projectile instanceof PersistentProjectileEntity persistentProjectile) {
			persistentProjectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		return projectile;
	}

	@WrapWithCondition(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"))
	private boolean enchancement$scatterShot(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot) {
		return !ScatterShotEffect.hasScatterShot;
	}

	@Inject(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"))
	private void enchancement$scatterShot(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci, @Local(ordinal = 1) ItemStack projectileStack) {
		if (!ScatterShotEffect.hasScatterShot && shouldApply(shooter, stack, projectileStack)) {
			ScatterShotEffect.hasScatterShot = true;
			for (int i = 0; i < shooter.getRandom().nextBetween(ScatterShotEffect.getMinimum(shooter, stack), ScatterShotEffect.getMaximum(shooter, stack)) - 1; i++) {
				shootAll(world, shooter, hand, stack, projectiles, speed, divergence, critical, target);
			}
			if (shooter instanceof PlayerEntity player) {
				player.getItemCooldownManager().set(stack.getItem(), 20);
			}
			ScatterShotEffect.hasScatterShot = false;
		}
	}

	@Unique
	private static boolean shouldApply(LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		Set<Item> allowedProjectiles = ScatterShotEffect.getAllowedProjectiles(shooter, weaponStack);
		return !(shooter instanceof PlayerEntity) || allowedProjectiles.contains(projectileStack.getItem());
	}
}
