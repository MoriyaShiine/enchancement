/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.scatter;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Unique
	private static int toShoot = 0;

	@Shadow
	private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
	}

	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$scatter(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (arrow.isOf(Items.AMETHYST_SHARD) || !(entity instanceof PlayerEntity)) {
			if (EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, crossbow)) {
				return new AmethystShardEntity(world, entity);
			}
		}
		return value;
	}

	@WrapWithCondition(method = "createArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"))
	private static boolean enchancement$scatter(PersistentProjectileEntity instance, boolean critical) {
		return !(instance instanceof AmethystShardEntity);
	}

	@ModifyVariable(method = "shoot", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private static float enchancement$scatterSpeed(float value, World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile) {
		if (toShoot == 0) {
			if (projectile.isOf(Items.AMETHYST_SHARD) || !(shooter instanceof PlayerEntity)) {
				if (EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, crossbow)) {
					return value / 2;
				}
			}
		}
		return value;
	}

	@ModifyVariable(method = "shoot", at = @At("HEAD"), ordinal = 2, argsOnly = true)
	private static float enchancement$scatterDivergence(float value, World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile) {
		if (toShoot == 0) {
			if (projectile.isOf(Items.AMETHYST_SHARD) || !(shooter instanceof PlayerEntity)) {
				if (EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, crossbow)) {
					return 16;
				}
			}
		}
		return value;
	}

	@Inject(method = "shoot", at = @At("TAIL"))
	private static void enchancement$scatter(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
		if (toShoot == 0) {
			if (projectile.isOf(Items.AMETHYST_SHARD) || !(shooter instanceof PlayerEntity)) {
				int level = EnchantmentHelper.getLevel(ModEnchantments.SCATTER, crossbow);
				if (level > 0) {
					if (shooter instanceof PlayerEntity player) {
						player.getItemCooldownManager().set(crossbow.getItem(), 20);
					}
					for (toShoot = MathHelper.nextInt(world.random, level * 6, level * 8) - 1; toShoot > 0; toShoot--) {
						shoot(world, shooter, hand, crossbow, projectile, soundPitch, creative, speed, divergence, simulated);
					}
				}
			}
		}
	}

	@WrapWithCondition(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
	private static boolean enchancement$scatterDamage(ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
		return toShoot == 0;
	}

	@WrapWithCondition(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private static boolean enchancement$scatterPlaySound(World instance, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		return toShoot == 0;
	}
}
