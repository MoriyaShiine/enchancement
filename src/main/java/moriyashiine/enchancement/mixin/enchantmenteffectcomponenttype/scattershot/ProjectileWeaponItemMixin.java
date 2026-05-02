/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.scattershot;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.world.item.effects.ScatterShotEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
	@Shadow
	protected abstract void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float power, float uncertainty, boolean isCrit, @Nullable LivingEntity targetOverride);

	@ModifyVariable(method = "createProjectile", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$scatterShot(boolean isCrit, Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile) {
		if (shouldApply(shooter, weapon, projectile)) {
			return false;
		}
		return isCrit;
	}

	@ModifyVariable(method = "shoot", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float enchancement$scatterShot(float uncertainty, ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles) {
		if (ScatterShotEffect.hasScatterShot) {
			return 16;
		}
		return uncertainty;
	}

	@WrapOperation(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private Projectile enchancement$scatterShot(ProjectileWeaponItem instance, Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit, Operation<Projectile> original) {
		Projectile projectileEntity = original.call(instance, level, shooter, weapon, projectile, isCrit);
		if (ScatterShotEffect.hasScatterShot && projectileEntity instanceof AbstractArrow arrow) {
			arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		}
		return projectileEntity;
	}

	@WrapWithCondition(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	private boolean enchancement$scatterShot(ItemStack instance, int amount, LivingEntity owner, EquipmentSlot slot) {
		return !ScatterShotEffect.hasScatterShot;
	}

	@Inject(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	private void enchancement$scatterShot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float power, float uncertainty, boolean isCrit, @Nullable LivingEntity targetOverride, CallbackInfo ci, @Local(name = "projectile") ItemStack projectile) {
		if (!ScatterShotEffect.hasScatterShot && shouldApply(shooter, weapon, projectile)) {
			ScatterShotEffect.hasScatterShot = true;
			for (int i = 0; i < shooter.getRandom().nextIntBetweenInclusive(ScatterShotEffect.getMinimum(shooter, weapon), ScatterShotEffect.getMaximum(shooter, weapon)) - 1; i++) {
				shoot(level, shooter, hand, weapon, projectiles, power, uncertainty, isCrit, targetOverride);
			}
			ScatterShotEffect.hasScatterShot = false;
		}
	}

	@Unique
	private static boolean shouldApply(LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		return !(shooter instanceof Player) || ScatterShotEffect.getAllowedProjectiles(shooter, weaponStack).contains(projectileStack.getItem());
	}
}
