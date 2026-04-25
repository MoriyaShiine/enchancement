/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandommobeffect;

import com.google.common.base.MoreObjects;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectGenericComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
	public AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "getPickupItem", at = @At("HEAD"), cancellable = true)
	private void enchancement$applyRandomMobEffect(CallbackInfoReturnable<ItemStack> cir) {
		ApplyRandomMobEffectComponent applyRandomMobEffectComponent = ModEntityComponents.APPLY_RANDOM_MOB_EFFECT.getNullable(this);
		if (applyRandomMobEffectComponent != null && !applyRandomMobEffectComponent.getOriginalStack().isEmpty()) {
			cir.setReturnValue(applyRandomMobEffectComponent.getOriginalStack());
		}
	}

	@Inject(method = "doPostHurtEffects", at = @At("TAIL"))
	private void enchancement$applyRandomMobEffect(LivingEntity mob, CallbackInfo ci) {
		ApplyRandomMobEffectGenericComponent applyRandomMobEffectGenericComponent = ModEntityComponents.APPLY_RANDOM_MOB_EFFECT_GENERIC.get(this);
		Entity source = MoreObjects.firstNonNull(getOwner(), this);
		applyRandomMobEffectGenericComponent.getEffects().forEach(effect -> mob.addEffect(effect, source));
	}
}
