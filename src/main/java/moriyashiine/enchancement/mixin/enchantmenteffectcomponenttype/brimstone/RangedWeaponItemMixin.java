/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyReturnValue(method = "createArrowEntity", at = @At("RETURN"))
	private ProjectileEntity enchancement$brimstone(ProjectileEntity original, World world, LivingEntity shooter, ItemStack weaponStack) {
		if (weaponStack.contains(ModComponentTypes.BRIMSTONE_DAMAGE)) {
			int damage = weaponStack.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
			if (world instanceof ServerWorld serverWorld) {
				shooter.damage(serverWorld, world.getDamageSources().create(ModDamageTypes.LIFE_DRAIN), shooter.getMaxHealth() * (damage / 20F));
			}
			BrimstoneEntity brimstone = new BrimstoneEntity(world, shooter, weaponStack);
			brimstone.setDamage(damage);
			return brimstone;
		}
		return original;
	}

	@WrapOperation(method = "method_61659", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void enchancement$brimstone(RangedWeaponItem instance, LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target, Operation<Void> original) {
		if (projectile instanceof BrimstoneEntity brimstone) {
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, shooter.getPitch());
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, shooter.getHeadYaw() + yaw);
			if (yaw != 0) {
				brimstone.setDamage(brimstone.getDamage() / 2);
			}
		}
		original.call(instance, shooter, projectile, index, speed, divergence, yaw, target);
	}

	@Inject(method = "shootAll", at = @At("TAIL"))
	private void enchancement$brimstone(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci) {
		int damage = stack.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (damage > 0) {
			stack.remove(ModComponentTypes.BRIMSTONE_DAMAGE);
			if (shooter instanceof PlayerEntity player) {
				player.getItemCooldownManager().set(stack, (int) (CrossbowItem.getPullTime(stack, shooter) * (damage / 12F)));
			}
		}
	}
}
