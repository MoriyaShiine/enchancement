/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandommobeffect;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectGenericComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Projectile.class)
public class ProjectileMixin {
	@ModifyReturnValue(method = "spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/world/entity/projectile/Projectile;", at = @At("RETURN"))
	private static <T extends Projectile> Projectile enchancement$applyRandomMobEffect(T original, @Local(argsOnly = true) ItemStack itemStack) {
		if (original.getOwner() instanceof LivingEntity owner) {
			ApplyRandomMobEffectComponent.maybeSet(owner, itemStack, ApplyRandomMobEffectComponent.getDurationMultiplier(owner, original.getDeltaMovement().length()), owner.getActiveItem(), effects -> {
				if (original instanceof Arrow arrow) {
					ModEntityComponents.APPLY_RANDOM_MOB_EFFECT.get(arrow).setOriginalStack(arrow.getPickupItem());
					arrow.setPotionContents(new PotionContents(Optional.empty(), Optional.empty(), effects, Optional.empty()));
				} else if (original instanceof AbstractArrow arrow) {
					ApplyRandomMobEffectGenericComponent applyRandomMobEffectGenericComponent = ModEntityComponents.APPLY_RANDOM_MOB_EFFECT_GENERIC.get(arrow);
					applyRandomMobEffectGenericComponent.setEffects(effects);
					applyRandomMobEffectGenericComponent.sync();
				}
			});
		}
		return original;
	}
}
