/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ArrowItem.class, SpectralArrowItem.class})
public class DelayArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$delay(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		int level = shooter instanceof PlayerEntity ? EnchantmentHelper.getLevel(ModEnchantments.DELAY, shooter.getActiveItem()) : EnchantmentHelper.getEquipmentLevel(ModEnchantments.DELAY, shooter);
		if (level > 0) {
			PersistentProjectileEntity arrow = cir.getReturnValue();
			ModEntityComponents.DELAY.maybeGet(arrow).ifPresent(delayComponent -> {
				delayComponent.setDelayLevel(level);
				delayComponent.setStackShotFrom(shooter.getActiveItem());
				delayComponent.sync();
			});
			arrow.setCritical(true);
		}
	}
}
