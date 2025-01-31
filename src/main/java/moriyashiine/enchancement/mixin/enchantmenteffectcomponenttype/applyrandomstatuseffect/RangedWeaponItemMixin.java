/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandomstatuseffect;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectComponent;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectGenericComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyExpressionValue(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;createArrowEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private ProjectileEntity enchancement$applyRandomStatusEffect(ProjectileEntity original, ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, @Local(ordinal = 1) ItemStack projectileStack) {
		if (original instanceof ArrowEntity arrow) {
			ApplyRandomStatusEffectComponent.maybeSet(shooter, projectileStack, ApplyRandomStatusEffectComponent.getDurationMultiplier(shooter, speed), stack, statusEffects -> {
				ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT.get(arrow).setOriginalStack(arrow.asItemStack());
				arrow.setPotionContents(new PotionContentsComponent(Optional.empty(), Optional.empty(), statusEffects, Optional.empty()));
			});
		} else if (original instanceof PersistentProjectileEntity projectile) {
			ApplyRandomStatusEffectComponent.maybeSet(shooter, projectileStack, ApplyRandomStatusEffectComponent.getDurationMultiplier(shooter, speed), stack, statusEffects -> {
				ApplyRandomStatusEffectGenericComponent applyRandomStatusEffectGenericComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_GENERIC.get(projectile);
				applyRandomStatusEffectGenericComponent.setEffects(statusEffects);
				applyRandomStatusEffectGenericComponent.sync();
			});
		}
		return original;
	}
}
