/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
	@ModifyReturnValue(method = "createProjectile", at = @At("RETURN"))
	private Projectile enchancement$brimstone(Projectile original, Level level, LivingEntity shooter, ItemStack weapon) {
		if (weapon.has(ModComponentTypes.BRIMSTONE_DAMAGE)) {
			int damage = weapon.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
			if (level instanceof ServerLevel serverLevel) {
				shooter.hurtServer(serverLevel, level.damageSources().source(ModDamageTypes.LIFE_DRAIN), shooter.getMaxHealth() * (damage / 20F));
			}
			Brimstone brimstone = new Brimstone(level, shooter, weapon);
			brimstone.setBaseDamage(damage);
			return brimstone;
		}
		return original;
	}

	@WrapOperation(method = "lambda$shoot$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"))
	private void enchancement$brimstone(ProjectileWeaponItem instance, LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target, Operation<Void> original) {
		if (projectile instanceof Brimstone brimstone) {
			brimstone.getEntityData().set(Brimstone.FORCED_X_ROT, shooter.getXRot());
			brimstone.getEntityData().set(Brimstone.FORCED_Y_ROT, shooter.getYHeadRot() + yaw);
			if (yaw != 0) {
				brimstone.setBaseDamage(brimstone.getDamage() / 2);
			}
		}
		original.call(instance, shooter, projectile, index, speed, divergence, yaw, target);
	}

	@Inject(method = "shoot", at = @At("TAIL"))
	private void enchancement$brimstone(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float power, float uncertainty, boolean isCrit, @Nullable LivingEntity targetOverride, CallbackInfo ci) {
		int damage = weapon.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (damage > 0) {
			weapon.remove(ModComponentTypes.BRIMSTONE_DAMAGE);
			if (shooter instanceof Player player) {
				player.getCooldowns().addCooldown(weapon, (int) (CrossbowItem.getChargeDuration(weapon, shooter) * (damage / 12F)));
			}
		}
	}
}
