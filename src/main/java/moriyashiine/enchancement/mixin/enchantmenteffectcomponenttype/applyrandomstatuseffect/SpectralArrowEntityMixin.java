/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandomstatuseffect;

import com.google.common.base.MoreObjects;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectSpectralComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowEntityMixin extends PersistentProjectileEntity {
	protected SpectralArrowEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onHit", at = @At("TAIL"))
	private void enchancement$applyRandomStatusEffect(LivingEntity target, CallbackInfo ci) {
		ApplyRandomStatusEffectSpectralComponent applyRandomStatusEffectSpectralComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_SPECTRAL.get(this);
		Entity source = MoreObjects.firstNonNull(getOwner(), this);
		applyRandomStatusEffectSpectralComponent.getEffects().forEach(effect -> target.addStatusEffect(effect, source));
	}
}
