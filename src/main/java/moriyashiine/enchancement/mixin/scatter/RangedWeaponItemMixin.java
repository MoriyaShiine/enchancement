/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.scatter;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
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

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {
	@Shadow
	protected abstract void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target);

	@ModifyVariable(method = "createArrowEntity", at = @At("HEAD"), argsOnly = true)
	private boolean enchancement$scatter(boolean value, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (isScatter(shooter, weaponStack, projectileStack)) {
			return false;
		}
		return value;
	}

	@ModifyVariable(method = "createArrowEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private PersistentProjectileEntity enchancement$scatter(PersistentProjectileEntity value, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (isScatter(shooter, weaponStack, projectileStack)) {
			return new AmethystShardEntity(world, shooter);
		}
		return value;
	}

	@ModifyVariable(method = "shootAll", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private float enchancement$scatter(float value, World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles) {
		if (isScatter(shooter, stack, projectiles)) {
			return 16;
		}
		return value;
	}

	@WrapWithCondition(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"))
	private boolean enchancement$scatter(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot) {
		return !EnchancementUtil.hasScatterShot;
	}

	@Inject(method = "shootAll", at = @At("TAIL"))
	private void enchancement$scatterTail(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci) {
		if (!EnchancementUtil.hasScatterShot && isScatter(shooter, stack, projectiles)) {
			EnchancementUtil.hasScatterShot = true;
			int level = EnchantmentHelper.getLevel(ModEnchantments.SCATTER, stack);
			for (int i = 0; i < MathHelper.nextInt(world.random, level * 6, level * 8) - 1; i++) {
				shootAll(world, shooter, hand, stack, projectiles, speed, divergence, critical, target);
			}
			EnchancementUtil.hasScatterShot = false;
		}
	}

	@Unique
	private static boolean isScatter(LivingEntity shooter, ItemStack weaponStack, List<ItemStack> projectiles) {
		for (ItemStack projectile : projectiles) {
			if (isScatter(shooter, weaponStack, projectile)) {
				return true;
			}
		}
		return false;
	}

	@Unique
	private static boolean isScatter(LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (projectileStack.isOf(Items.AMETHYST_SHARD) || !(shooter instanceof PlayerEntity)) {
			int level = EnchantmentHelper.getLevel(ModEnchantments.SCATTER, weaponStack);
			return level > 0;
		}
		return false;
	}
}
