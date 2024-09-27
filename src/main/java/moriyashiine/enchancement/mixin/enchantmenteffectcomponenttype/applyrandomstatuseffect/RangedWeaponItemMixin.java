/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandomstatuseffect;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectComponent;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectSpectralComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.mixin.util.accessor.ArrowEntityAccessor;
import moriyashiine.enchancement.mixin.util.accessor.PersistentProjectileEntityAccessor;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@Inject(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"))
	private void enchancement$teleportOnHit(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, CallbackInfo ci, @Local(ordinal = 1) ItemStack projectileStack, @Local ProjectileEntity projectileEntity) {
		if (projectileEntity instanceof ArrowEntity arrow) {
			ApplyRandomStatusEffectComponent.maybeSet(shooter, projectileStack, stack, statusEffects -> {
				ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT.get(arrow).setOriginalStack(((PersistentProjectileEntityAccessor) arrow).enchancement$asItemStack());
				((ArrowEntityAccessor) arrow).enchancement$setPotionContents(new PotionContentsComponent(Optional.empty(), Optional.empty(), statusEffects));
			});
		}
		if (projectileEntity instanceof SpectralArrowEntity spectralArrow) {
			ApplyRandomStatusEffectComponent.maybeSet(shooter, projectileStack, stack, statusEffects -> {
				ApplyRandomStatusEffectSpectralComponent applyRandomStatusEffectSpectralComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_SPECTRAL.get(spectralArrow);
				applyRandomStatusEffectSpectralComponent.setEffects(statusEffects);
				applyRandomStatusEffectSpectralComponent.sync();
			});
		}
	}
}
