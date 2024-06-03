/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.brimstone;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyVariable(method = "createArrowEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private PersistentProjectileEntity enchancement$brimstone(PersistentProjectileEntity value, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (weaponStack.contains(ModDataComponentTypes.BRIMSTONE_DAMAGE)) {
			int damage = weaponStack.getOrDefault(ModDataComponentTypes.BRIMSTONE_DAMAGE, 0);
			shooter.damage(ModDamageTypes.create(world, ModDamageTypes.LIFE_DRAIN), shooter.getMaxHealth() * (damage / 20F));
			BrimstoneEntity brimstone = new BrimstoneEntity(world, shooter);
			brimstone.setDamage(damage);
			return brimstone;
		}
		return value;
	}

	@WrapOperation(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void enchancement$brimstone(RangedWeaponItem instance, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target, Operation<Void> original) {
		if (projectile instanceof BrimstoneEntity brimstone) {
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, shooter.getPitch());
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, shooter.getYaw() + yaw);
			if (yaw != 0) {
				brimstone.setDamage(brimstone.getDamage() / 2);
			}
		}
		original.call(instance, shooter, projectile, index, speed, divergence, yaw, target);
	}

	@Inject(method = "shootAll", at = @At("TAIL"))
	private void enchancement$brimstone(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci) {
		int damage = stack.getOrDefault(ModDataComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (damage > 0) {
			stack.remove(ModDataComponentTypes.BRIMSTONE_DAMAGE);
			if (shooter instanceof PlayerEntity player) {
				player.getItemCooldownManager().set(stack.getItem(), (int) (CrossbowItem.getPullTime(stack) * (damage / 12F)));
			}
		}
	}
}
