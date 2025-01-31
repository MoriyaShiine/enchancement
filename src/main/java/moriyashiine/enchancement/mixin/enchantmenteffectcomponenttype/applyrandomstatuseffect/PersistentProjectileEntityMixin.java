/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandomstatuseffect;

import com.google.common.base.MoreObjects;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectComponent;
import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectGenericComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
	public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "asItemStack", at = @At("HEAD"), cancellable = true)
	private void enchancement$applyRandomStatusEffect(CallbackInfoReturnable<ItemStack> cir) {
		ApplyRandomStatusEffectComponent applyRandomStatusEffectComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT.getNullable(this);
		if (applyRandomStatusEffectComponent != null && !applyRandomStatusEffectComponent.getOriginalStack().isEmpty()) {
			cir.setReturnValue(applyRandomStatusEffectComponent.getOriginalStack());
		}
	}

	@Inject(method = "onHit", at = @At("TAIL"))
	private void enchancement$applyRandomStatusEffect(LivingEntity target, CallbackInfo ci) {
		ApplyRandomStatusEffectGenericComponent applyRandomStatusEffectGenericComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_GENERIC.get(this);
		Entity source = MoreObjects.firstNonNull(getOwner(), this);
		applyRandomStatusEffectGenericComponent.getEffects().forEach(effect -> target.addStatusEffect(effect, source));
	}
}
