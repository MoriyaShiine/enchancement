/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandommobeffect;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectGenericComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
	@ModifyExpressionValue(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private Projectile enchancement$applyRandomMobEffect(Projectile original, ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float power, @Local(name = "projectile") ItemStack projectile) {
		if (original instanceof Arrow arrow) {
			ApplyRandomMobEffectComponent.maybeSet(shooter, projectile, ApplyRandomMobEffectComponent.getDurationMultiplier(shooter, power), weapon, statusEffects -> {
				ModEntityComponents.APPLY_RANDOM_MOB_EFFECT.get(arrow).setOriginalStack(arrow.getPickupItem());
				arrow.setPotionContents(new PotionContents(Optional.empty(), Optional.empty(), statusEffects, Optional.empty()));
			});
		} else if (original instanceof AbstractArrow arrow) {
			ApplyRandomMobEffectComponent.maybeSet(shooter, projectile, ApplyRandomMobEffectComponent.getDurationMultiplier(shooter, power), weapon, statusEffects -> {
				ApplyRandomMobEffectGenericComponent applyRandomMobEffectGenericComponent = ModEntityComponents.APPLY_RANDOM_MOB_EFFECT_GENERIC.get(arrow);
				applyRandomMobEffectGenericComponent.setEffects(statusEffects);
				applyRandomMobEffectGenericComponent.sync();
			});
		}
		return original;
	}
}
